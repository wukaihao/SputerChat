package chat.tox.antox.activities

import java.io.{File, IOException}
import java.text.SimpleDateFormat
import java.util.Date

import android.app.Activity
import android.content.{Context, Intent}
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.{Build, Bundle, Environment}
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import android.view.View
import android.widget._
import chat.tox.antox.R
import chat.tox.antox.data.State
import chat.tox.antox.theme.ThemeManager
import chat.tox.antox.tox.{MessageHelper, ToxSingleton}
import chat.tox.antox.transfer.FileDialog
import chat.tox.antox.utils.StringExtensions.RichString
import chat.tox.antox.utils._
import chat.tox.antox.wrapper._
import de.hdodenhof.circleimageview.CircleImageView
import im.tox.tox4j.core.data.ToxFileId
import im.tox.tox4j.core.enums.ToxMessageType
import im.tox.tox4j.exceptions.ToxException
import rx.lang.scala.schedulers.{AndroidMainThreadScheduler, IOScheduler}

class ChatActivity extends GenericChatActivity[FriendKey] {

  val photoPathSaveKey = "PHOTO_PATH"
  var photoPath: Option[String] = None

  override def getKey(key: String): FriendKey = new FriendKey(key)

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    val thisActivity = this
    getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    ThemeManager.applyTheme(this, getSupportActionBar)   //设置actionbar颜色的调用方法
//    getSupportActionBar.setBackgroundDrawable(new ColorDrawable(R.color.green_darker))

    /*this.findViewById(R.id.info).setVisibility(View.GONE)*/

    /* Set up on click actions for attachment buttons. Could possible just add onClick to the XML?? */
    //绑定 相机 照片 三个按键
    val attachmentButton = this.findViewById(R.id.attachment_button)
    val cameraButton = this.findViewById(R.id.camera_button)
    val imageButton = this.findViewById(R.id.image_button)
    //绑定点击事件
    attachmentButton.setOnClickListener(new View.OnClickListener() {
      override def onClick(v: View) {
        val friendInfo = State.db.getFriendInfo(activeKey)
        if (!friendInfo.online) {
          Toast.makeText(thisActivity, getResources.getString(R.string.chat_ft_failed_friend_offline), Toast.LENGTH_SHORT).show()
          return
        }
        val path = new File(Environment.getExternalStorageDirectory + "//DIR//")
        val fileDialog = new FileDialog(thisActivity, path, false)
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
          def fileSelected(file: File) {
            State.transfers.sendFileSendRequest(file.getPath, activeKey, FileKind.DATA, ToxFileId.empty, thisActivity)
          }
        })
        fileDialog.showDialog()

      }
    })
     //相机点击事件
    cameraButton.setOnClickListener(new View.OnClickListener() {
      override def onClick(v: View) {
        val friendInfo = State.db.getFriendInfo(activeKey)
        if (!friendInfo.online) {
          Toast.makeText(thisActivity, getResources.getString(R.string.chat_ft_failed_friend_offline), Toast.LENGTH_SHORT).show()
          return
        }

        val cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        val image_name = "WangWangpic " + new SimpleDateFormat("hhmm").format(new Date()) + " "             //照片的存储路径  Antoxpic

        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        try {
          val file = File.createTempFile(image_name, ".jpg", storageDir)
          val imageUri = Uri.fromFile(file)
          cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
          photoPath = Some(file.getAbsolutePath)
          startActivityForResult(cameraIntent, Constants.PHOTO_RESULT)
        } catch {
          case e: IOException => e.printStackTrace()
        }

      }
    })
      //相册点击事件
    imageButton.setOnClickListener(new View.OnClickListener() {
      override def onClick(v: View) {
        val friendInfo = State.db.getFriendInfo(activeKey)
        if (!friendInfo.online) {
          Toast.makeText(thisActivity, getResources.getString(R.string.chat_ft_failed_friend_offline), Toast.LENGTH_SHORT).show()
          return
        }

        val intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, Constants.IMAGE_RESULT)
      }
    })
  }
  //存储数据  等下次再打开的时候调用
  override def onSaveInstanceState(savedInstanceState: Bundle): Unit = {
    super.onSaveInstanceState(savedInstanceState)
    //save the photo path to prevent it being lost on rotation
    savedInstanceState.putString(photoPathSaveKey, photoPath.getOrElse(""))
  }

  override def onRestoreInstanceState(savedInstanceState: Bundle): Unit = {
    super.onRestoreInstanceState(savedInstanceState)
    val rawPhotoPath = savedInstanceState.getString(photoPathSaveKey)
    photoPath = rawPhotoPath.toOption
  }
//运行过程中序列化好友的信息
  override def onResume(): Unit = {
    super.onResume()
    //clear notifications that have already been seen
    AntoxNotificationManager.clearMessageNotification(activeKey)

    titleSub = State.db.friendInfoList
      .subscribeOn(IOScheduler())
      .observeOn(AndroidMainThreadScheduler())
      .subscribe(fi => {
      updateDisplayedState(fi)
    })
  }

  private def updateDisplayedState(friendInfoList: Seq[FriendInfo]): Unit = {
    val thisActivity = this
    val key = activeKey
    //序列化好友信息
    val mFriend: Option[FriendInfo] = friendInfoList.find(f => f.key == key)
    mFriend match {
      case Some(friend) =>
        thisActivity.setDisplayName(friend.getDisplayName)
        val avatar = friend.avatar
        avatar.foreach(avatar => {
          val avatarView = this.findViewById(R.id.avatar).asInstanceOf[CircleImageView]
          BitmapManager.load(avatar, isAvatar = true).foreach(avatarView.setImageBitmap)
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          thisActivity.statusIconView.setBackground(thisActivity.getResources
            .getDrawable(IconColor.iconDrawable(friend.online, UserStatus.getToxUserStatusFromString(friend.status))))
        } else {
          thisActivity.statusIconView.setBackgroundDrawable(thisActivity.getResources
            .getDrawable(IconColor.iconDrawable(friend.online, UserStatus.getToxUserStatusFromString(friend.status))))
        }

      case None =>
        thisActivity.setDisplayName("")
    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == Constants.IMAGE_RESULT) {
        val uri = data.getData
        val filePathColumn = Array(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
        val loader = new CursorLoader(this, uri, filePathColumn, null, null, null)
        val cursor = loader.loadInBackground()
        if (cursor != null) {
          if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(filePathColumn(0))
            val filePath = cursor.getString(columnIndex)
            val fileNameIndex = cursor.getColumnIndexOrThrow(filePathColumn(1))
            val fileName = cursor.getString(fileNameIndex)
            try {
              State.transfers.sendFileSendRequest(filePath, this.activeKey, FileKind.DATA, ToxFileId.empty, this)
            } catch {
              case e: Exception => e.printStackTrace()
            }
          }
        }
      }
      if (requestCode == Constants.PHOTO_RESULT) {
          photoPath.foreach(path => State.transfers.sendFileSendRequest(path, this.activeKey, FileKind.DATA, ToxFileId.empty, this))
          photoPath = None
      }
    } else {
      AntoxLog.debug("onActivityResult result code not okay, user cancelled")
    }
  }

  def onClickVoiceCallFriend(v: View) {}

  def onClickVideoCallFriend(v: View): Unit = {}

  def onClickInfo(v: View): Unit = {}

  override def sendMessage(message: String, messageType: ToxMessageType, context: Context): Unit = {
    MessageHelper.sendMessage(this, activeKey, message, messageType, None)
  }

  override def setTyping(typing: Boolean): Unit = {
    try {
      ToxSingleton.tox.setTyping(activeKey, typing)
    } catch {
      case te: ToxException[_] =>
      case e: Exception =>
    }
  }
}
