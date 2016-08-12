package com.hmlee.chat.chatclient;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hmlee.chat.chatclient.db.ConversationItemData;
import com.hmlee.chat.chatclient.db.DataChange;
import com.hmlee.chat.chatclient.db.DataObserver;
import com.hmlee.chat.chatclient.db.MessageDB;
import com.hmlee.chat.chatclient.utils.ConfigSettingPreferences;
import com.hmlee.chat.chatclient.widget.OkCancelDialog;
import com.hmlee.chat.chatclient.widget.SwipeDismissListViewTouchListener;

import java.util.ArrayList;

public class ConversationsFragment extends Fragment implements android.view.View.OnClickListener, DataChange {

    private ListView mListView;
    private ConversationsAdapter mAdapter;
    private Activity mActivity;
    
    private FrameLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private CheckBox mAllConversationCheck;
    private Button mDeleteButton;
    private SwipeDismissListViewTouchListener mTouchListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations, container, false);

        findViews(rootView);
        initViews();
        return rootView;
    }
    
    

    @Override
    public void onResume() {
        super.onResume();
        DataObserver.getInstance().addObserver(this);
        queryConversations();
        ConfigSettingPreferences.getInstance(mActivity).releaseNotification();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_conversations, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_move_message:
            // TODO :: 추후 단체 메시지 등 기능 추가 필요
//            MessageDB.getInstance().storeMessage(mActivity, CommonUtils.MESSAGE, "01047323972", "hmlee", "test message", MessageDB.RECEIVE_TYPE);
//            Intent intent = new Intent(mActivity, ComposeActivity.class);
//            intent.putExtra(ComposeActivity.SEND_FROM, ComposeActivity.SEND_FROM_CONVERSATION);
//            mActivity.startActivity(intent);
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void findViews(View rootView) {
        mTopLayout = (FrameLayout) rootView.findViewById(R.id.layout_top_conversation);
        mListView = (ListView) rootView.findViewById(R.id.conversations_listview);
        mBottomLayout = (LinearLayout) rootView.findViewById(R.id.layout_bottom_conversation);
        mAllConversationCheck = (CheckBox) rootView.findViewById(R.id.check_all_conversation);
        mDeleteButton = (Button) rootView.findViewById(R.id.btn_delete_conversation);
    }

    private void initViews() {
        mAdapter = new ConversationsAdapter(mActivity);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mItemClickListener);
        mListView.setOnItemLongClickListener(mItemLongClickListener);
        mListView.setScrollingCacheEnabled(false);
        
        mDeleteButton.setOnClickListener(this);
        mAllConversationCheck.setOnClickListener(this);

        setHasOptionsMenu(true);
        
        mTouchListener = new SwipeDismissListViewTouchListener(mListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
			@Override
			public boolean canDismiss(int position) {
			    
				return true;
			}

			@Override
			public void onDismiss(ListView listView, int[] reverseSortedPositions) {
				//mAdapter.removeItem(reverseSortedPositions);
			    showMultimediaDeleteConfirmDialog(reverseSortedPositions);
			}
		});
		mListView.setOnTouchListener(mTouchListener);
		mListView.setOnScrollListener(mTouchListener.makeScrollListener());
    }

    public void showMultimediaDeleteConfirmDialog(final int[] reverseSortedPositions) {
        OkCancelDialog.newInstance(R.string.delete, R.string.delete_conversation, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    mAdapter.removeItem(reverseSortedPositions);
                    break;
                default:
                    break;
                }
            }
        }).show(getFragmentManager(), "MultimediaDeleteConfirmDialog");
    }
    
    private void startDeleteMode() {
        mTopLayout.setVisibility(View.VISIBLE);
        mBottomLayout.setVisibility(View.VISIBLE);
        mAdapter.startDeleteMode();
        mTouchListener.setActiveEnabled(false);
    }

    public boolean finishDeleteMode() {
        if (mAdapter != null && mAdapter.isDeleteMode()) {
            mTopLayout.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            mTouchListener.setActiveEnabled(true);
            mAdapter.finishDeleteMode();
            initListCheckAllCancel();
            setHasOptionsMenu(true);
            return true;
        }
        return false;
    }

    protected void queryConversations() {
        new Thread() {
            public void run() {
                Message message = Message.obtain();
                Cursor cursor = MessageDB.getInstance().queryForAllConversation(mActivity);
                message.obj = cursor;
                mMessageHandler.sendMessage(message);
            }
        }.start();
    }

    private Handler mMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            mAdapter.setCursor(null);
            Cursor cursor = (Cursor) msg.obj;
            mAdapter.setCursor(cursor);
            mAdapter.notifyDataSetChanged();
        };
    };
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        
        case R.id.btn_delete_conversation:
            showDeleteConfirmDialog();
            break;
            
        case R.id.check_all_conversation:
            if (mAllConversationCheck.isChecked()) {
                initListCheckAll();
            } else {
                initListCheckAllCancel();
            }
            break;
        }
        
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mAdapter.isDeleteMode()) {
                checkedItemAction(position);
            } else {
                ConversationItemData item  = (ConversationItemData) mAdapter.getItem(position);
                long threadId = item.getThreadId();
                Intent intent = new Intent(mActivity, MessageActivity.class)
                    .putExtra(MessageActivity.THREAD_ID, threadId);

                // .putExtra(MessagesActivity.EXTRA_THREAD_RECIPIENTS,
                // cursor.getString(cursor.getColumnIndexOrThrow("recipient_ids")));
                // Intent intent = new Intent(mActivity,
                // MessagesActivity.class);
                startActivity(intent);
            }
        }
    };

    private OnItemLongClickListener mItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!mAdapter.isDeleteMode()) {
                startDeleteMode();
            }
            return true;
        }
    };
    
    public void onPause() {
        DataObserver.getInstance().removeObserver(this);
        super.onPause();
    };
    
    private void checkedItemAction(int position) {
        mAdapter.getListItems().get(position)
            .setCheck(!mAdapter.getListItems().get(position).isChecked());
        mAdapter.notifyDataSetChanged();

        int totalCount = mAdapter.getCheckedTotalCount();
        if (totalCount > 0) {
            mDeleteButton.setEnabled(true);
        } else {
            mDeleteButton.setEnabled(false);
        }
        
        if (totalCount != mAdapter.getListItems().size()) {
            mAllConversationCheck.setChecked(false);
        }
        else {
            mAllConversationCheck.setChecked(true);
        }
    }

    private void initListCheckAll() {
        for (int i = 0; i < mAdapter.getListItems().size(); i++) {
            mAdapter.getListItems().get(i).setCheck(true);
        }
        mAdapter.notifyDataSetChanged();
        mAllConversationCheck.setChecked(true);
        mDeleteButton.setEnabled(true);
    }

    private void initListCheckAllCancel() {
        for (int i = 0; i < mAdapter.getListItems().size(); i++) {
            mAdapter.getListItems().get(i).setCheck(false);
        }
        mAdapter.notifyDataSetChanged();
        mAllConversationCheck.setChecked(false);
        mDeleteButton.setEnabled(false);
    }

    public void showDeleteConfirmDialog() {
        OkCancelDialog.newInstance(R.string.delete, R.string.delete_message, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    deleteTransaction();
                    break;
                default:
                    // ignore
                    break;
                }
            }
        }).show(getFragmentManager(), "DeleteConfirmDialog");
    }
    
    private ArrayList<ConversationItemData> getCheckedItems() {
        if (mAdapter != null) {
            return mAdapter.getCheckedItems();
        } else {
            return new ArrayList<ConversationItemData>();
        }
    }
    
    private void deleteTransaction() {
        ArrayList<ConversationItemData> checkedItems = getCheckedItems();
        final String[] threadIds = new String[checkedItems.size()];
        
        for (int i = 0; i < checkedItems.size(); i++) {
            threadIds[i] = Long.toString(checkedItems.get(i).getThreadId());
        }
        
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                MessageDB.getInstance().deleteConversations(mActivity, threadIds);
                MessageDB.getInstance().deleteMessages(mActivity, threadIds);
                MessageDB.getInstance().deleteThreads(mActivity, threadIds);
                
                return null;
            }

            @Override
            protected void onPostExecute(String msg) {
                Message message = Message.obtain();
                message.obj = MessageDB.getInstance().queryForAllConversation(mActivity);
                mMessageHandler.sendMessage(message);
                finishDeleteMode();
                
                Toast.makeText(mActivity, "메시지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }


    public boolean handleBackPressed() {
        boolean state = false;
        state = finishDeleteMode();
        
        return state;
    }

    @Override
    public void onChange() {
        queryConversations();
    }

}
