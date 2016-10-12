package chat.tox.antox.fragments

import java.util

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View.OnClickListener
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.{LinearLayout, TextView, Button}
import chat.tox.antox.R
import chat.tox.antox.activities.{Main4Activity, GroupChatActivity}
import chat.tox.antox.adapters.ContactListAdapter
import chat.tox.antox.applaction.MyApplaction
import chat.tox.antox.data.State
import chat.tox.antox.utils.{AntoxLog, LeftPaneItem, TimestampUtils}
import chat.tox.antox.wrapper.{FriendInfo, FriendRequest, GroupInfo, GroupInvite}
import im.tox.tox4j.core.enums.ToxUserStatus
import rx.lang.scala.schedulers.AndroidMainThreadScheduler
import wangwang.util.Cn2Spell
//联系人
class ContactsFragment extends AbstractContactsFragment(showSearch = true, showFab = true) {
  var button_all : Button = _
  var button_online : Button = _
  var text_requestmessage : TextView = _
  var text_saomiao : TextView = _
  var text_tongxunlu : TextView = _
  var a = 0
  var db = State.db
  override def updateContacts(contactInfoTuple: (Seq[FriendInfo], Seq[FriendRequest],
    Seq[GroupInvite], Seq[GroupInfo])) {
    contactInfoTuple match {
      case (friendsList, friendRequests, groupInvites, groupList) =>
        leftPaneAdapter = new ContactListAdapter(getActivity)
        updateFriendsList(leftPaneAdapter, friendsList)
        updateFriendRequests(leftPaneAdapter, friendRequests)
        updateGroupInvites(leftPaneAdapter, groupInvites)
        updateGroupList(leftPaneAdapter, groupList)

        contactsListView.setAdapter(leftPaneAdapter)
//        AntoxLog.debug(friendsList+"",AntoxLog.DEFAULT_TAG)

        val list=new util.ArrayList[FriendInfo]()
        for(friendinfo <-friendsList){
          list.add(friendinfo)
//          AntoxLog.debug(list+"",AntoxLog.DEFAULT_TAG)
        }
        MyApplaction.getIntance.setFriendInfoList(list)
    }
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val rootView = super.onCreateView(inflater, container, savedInstanceState)
    /*rootView.findViewById(R.id.center_text).setVisibility(View.GONE)*/
    button_all=rootView.findViewById(R.id.button_all).asInstanceOf[Button]
    button_online=rootView.findViewById(R.id.button_online).asInstanceOf[Button]
    text_requestmessage=rootView.findViewById(R.id.text_requestmessage).asInstanceOf[TextView]
    text_saomiao=rootView.findViewById(R.id.text_saomiao).asInstanceOf[TextView]
    text_tongxunlu=rootView.findViewById(R.id.text_tongxunlu).asInstanceOf[TextView]
    button_all.setBackgroundColor(Color.parseColor("#007bff"))
    //所有联系人
    button_all.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        if (a != 0){
          a = 0
          button_all.setBackgroundColor(Color.parseColor("#007bff"))
          button_online.setBackgroundColor(Color.parseColor("#ffffff"))
          contactChangeSub = db.contactListElements
            .observeOn(AndroidMainThreadScheduler())
            .subscribe(updateContacts(_))
        }
      }
    })
    //在線的點擊事件
    button_online.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        a = 1
        button_online.setBackgroundColor(Color.parseColor("#007bff"))
        button_all.setBackgroundColor(Color.parseColor("#ffffff"))
        contactChangeSub = db.contactListElements
          .observeOn(AndroidMainThreadScheduler())
          .subscribe(updateContacts(_))
      }
    })
    //消息请求
    text_requestmessage.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = ???
        /*new Intent(getActivity, classOf[])*/
    })
    text_saomiao.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit =
        startActivity(new Intent(getActivity, classOf[Main4Activity]))
    })
    text_tongxunlu.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit =
        startActivity(new Intent(Intent.ACTION_CALL_BUTTON))
    })
    rootView
  }
  //更新好友列表
  def updateFriendsList(leftPaneAdapter: ContactListAdapter, friendsList: Seq[FriendInfo]): Unit = {
    var sortedFriendsList = friendsList.sortWith(compareNames).sortWith(compareOnline).sortWith(compareFavorite)
    var list:List[String]  = List()
    for (s <- sortedFriendsList) {
      var string: String = Cn2Spell.getPinYin(s.getDisplayName).substring(0, 1).toUpperCase()
      if (string.matches("[A-Z]"))
        list = string :: list
      else list = "#" :: list
    }
    list=list.filter(_!= "#")
    list=list.sortWith(_.toLowerCase<_.toLowerCase)
    var list1= List("#")
    list=list:::list1

   /* s.sortWith(_.toLowerCase < _.toLowerCase)
    res4: List = List(a, B, d, e, F)*/

    if (sortedFriendsList.nonEmpty) {
      if (a==0) {
        for (l <- list) {
          for (f <- sortedFriendsList) {
            var string1=Cn2Spell.getPinYin(f.getDisplayName).substring(0, 1).toUpperCase()
            if (string1.matches("[A-Z]")){}
            else  {string1="#"}

            if (l == string1) {
              val friend = new LeftPaneItem(ContactItemType.FRIEND, f.key, f.avatar, f.getDisplayName, f.statusMessage,
                f.online, f.getFriendStatusAsToxUserStatus, f.favorite, f.unreadCount,
                f.lastMessage.map(_.timestamp).getOrElse(TimestampUtils.emptyTimestamp()))
              leftPaneAdapter.addItem(friend)
            }
          }
        }
      }


      if (a==1){
        for (l <- list){
         for (f <- sortedFriendsList) {
           if (f.online) {
             var string1 = Cn2Spell.getPinYin(f.getDisplayName).substring(0, 1).toUpperCase()
             if (string1.matches("[A-Z]")) {}
             else {
               string1 = "#"
             }

             if (l == string1) {
               val friend = new LeftPaneItem(ContactItemType.FRIEND, f.key, f.avatar, f.getDisplayName, f.statusMessage,
                 f.online, f.getFriendStatusAsToxUserStatus, f.favorite, f.unreadCount,
                 f.lastMessage.map(_.timestamp).getOrElse(TimestampUtils.emptyTimestamp()))
               leftPaneAdapter.addItem(friend)
             }
           }
         }
        }
      }
    }
  }

  def updateFriendRequests(leftPaneAdapter: ContactListAdapter, friendRequests: Seq[FriendRequest]): Unit = {
    if (friendRequests.nonEmpty) {
      for (r <- friendRequests) {
        val request = new LeftPaneItem(ContactItemType.FRIEND_REQUEST, r.requestKey, r.requestMessage)
        leftPaneAdapter.insert(0, request) // insert friend requests at top of contact list
      }
    }
  }

  def updateGroupInvites(leftPaneAdapter: ContactListAdapter, groupInvites: Seq[GroupInvite]): Unit = {
    if (groupInvites.nonEmpty) {
      for (invite <- groupInvites) {
        val request = new LeftPaneItem(ContactItemType.GROUP_INVITE, invite.groupKey, getResources.getString(R.string.invited_by) + " " + invite.inviter)
        leftPaneAdapter.addItem(request)
      }
    }
  }

  def updateGroupList(leftPaneAdapter: ContactListAdapter, groups: Seq[GroupInfo]): Unit = {
    val sortedGroupList = groups.sortWith(compareNames).sortWith(compareFavorite)
    if (sortedGroupList.nonEmpty) {
      for (group <- sortedGroupList) {
        val groupPane: LeftPaneItem = new LeftPaneItem(ContactItemType.GROUP, group.key, group.avatar, group.getDisplayName, group.topic,
          group.online, ToxUserStatus.NONE, group.favorite, group.unreadCount, group.lastMessage.map(_.timestamp).getOrElse(TimestampUtils.emptyTimestamp()))
        leftPaneAdapter.addItem(groupPane)
      }
    }
  }
}
