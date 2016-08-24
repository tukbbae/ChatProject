package com.hmlee.chat.chatclient;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hmlee.chat.chatclient.db.DataChange;
import com.hmlee.chat.chatclient.db.DataObserver;
import com.hmlee.chat.chatclient.db.MessageDB;
import com.hmlee.chat.chatclient.db.RecipientIdCache;
import com.hmlee.chat.chatclient.http.HttpClient;
import com.hmlee.chat.chatclient.http.model.CommonResponse;
import com.hmlee.chat.chatclient.http.model.MessageRequest;
import com.hmlee.chat.chatclient.utils.CommonUtils;
import com.hmlee.chat.chatclient.utils.ConfigSettingPreferences;
import com.hmlee.chat.chatclient.widget.MonitoringEditText;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.IOException;
import java.net.ConnectException;

public class MessageFragment extends Fragment implements DataChange, MonitoringEditText.OnPasteListener {

    public static final String RESPONSE_MSG = "response_msg";
    public static final String API_ID_MESSAGE_REQUEST = "message_request";

    private SendMessageTask mSendTask;

    // HTTP Client
    private HttpClient mHttpClient;

    private Activity mActivity;
    private MessageAdapter mAdapter;
    private long mThreadId;
    private ImageView mBackgroundImage;
    private ImageView mImgProgress;
    private ListView mListView;

    private MonitoringEditText mInputMessage;
    private Button mSendButton;

    private DisplayImageOptions mOptions;
    private ImageLoader mImageLoader = ImageLoader.getInstance();

    // TODO :: 추후 다른 메시지 타입이 있을 시 처리하기 위한 Type
    private int mSendType = 0; // 0 normal
    
    final Handler responseHandler = new Handler() {
        public void handleMessage(Message msg) {
            mSendButton.setVisibility(View.VISIBLE);
            mImgProgress.setVisibility(View.GONE);

            String response = msg.getData().getString(RESPONSE_MSG);
            if (response.equals(API_ID_MESSAGE_REQUEST)) {
                boolean result = msg.getData().getBoolean("result");

                if (result) {
//                    Toast.makeText(mActivity, "메시지 전송에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "메시지 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }

            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        mActivity = getActivity();
        mThreadId = mActivity.getIntent().getLongExtra(MessageActivity.THREAD_ID, -1);

        findViews(rootView);
        initViews();
        initHttpModule();

        if (mThreadId > 0) {
            queryMessages(mThreadId);
        }
        DataObserver.getInstance().addObserver(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUnreadCount();
        setBackground();
    }

    private void findViews(View rootView) {
        mBackgroundImage = (ImageView) rootView.findViewById(R.id.message_background);
        mListView = (ListView) rootView.findViewById(R.id.messages_listview);
        mInputMessage = (MonitoringEditText) rootView.findViewById(R.id.message_text_editor);
        mSendButton = (Button) rootView.findViewById(R.id.message_send_btn);
        mImgProgress = (ImageView) rootView.findViewById(R.id.message_img_progress);
    }

    private void initViews() {
        mAdapter = new MessageAdapter(mActivity, mListView, null, true, mThreadId);

        mListView.setAdapter(mAdapter);
        mListView.setScrollingCacheEnabled(false);
        mSendButton.setOnClickListener(mSendButtonClickListener);

        mOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.skin_preview_00)
                .showImageOnFail(R.mipmap.skin_preview_00).cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer()).build();
        mInputMessage.setOnPasteListener(this);
    }

    private void initHttpModule() {
        mHttpClient = new HttpClient(mActivity, CommonUtils.SERVER_URL, null);
    }

    @Override
    public void onTextPaste(boolean flag) {
        refreshView();
    }

    private void refreshView() {
        // Logger.e(TAG, "refreshView");
        mInputMessage.postDelayed(new Runnable() {

            @Override
            public void run() {
                String str = mInputMessage.getEditableText().toString();
                int index = mInputMessage.getSelectionStart();
                mInputMessage.setText("");
                mInputMessage.setSelection(index);
            }
        }, 50);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void setBackground() {
        String uri = ConfigSettingPreferences.getInstance(mActivity).getPrefBackgroundUri();
        mImageLoader.displayImage(uri, mBackgroundImage, mOptions);
    }

    private void queryMessages(final long threadId) {
        new Thread() {
            public void run() {
                Message message = Message.obtain();
                message.obj = MessageDB.getInstance().queryForMessages(mActivity, mThreadId);
                mMessageHandler.sendMessage(message);
            }
        }.start();
    }

    private Handler mMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            Cursor cursor = (Cursor) msg.obj;
            mAdapter.changeCursor(null);
            mAdapter.changeCursor(cursor);
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mAdapter.getCount() - 1);
        };
    };

    @Override
    public void onDestroy() {
        mAdapter.changeCursor(null);
        DataObserver.getInstance().removeObserver(this);
        super.onDestroy();
    }

    private void updateUnreadCount() {
        new Thread() {
            public void run() {
                MessageDB.getInstance().initUnreadCount(mActivity, mThreadId);

                int unreadCount = MessageDB.getInstance().queryForUnreadCount(mActivity);
                CommonUtils.updateIconBadge(mActivity, unreadCount);
            }
        }.start();

    }

    @Override
    public void onChange() {
        if (mThreadId > 0) {
            queryMessages(mThreadId);
            updateUnreadCount();
        }
    }

    private OnClickListener mSendButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            prePareSendPushMessage(v);
        }
    };

    private void prePareSendPushMessage(View v) {
        String message = mInputMessage.getText().toString();
        if (message.length() > 0) {
            v.setVisibility(View.GONE);
            mImgProgress.setVisibility(View.VISIBLE);
            AnimationDrawable frameAnimation = (AnimationDrawable) mImgProgress.getBackground();
            frameAnimation.start();

            String receiverName = RecipientIdCache.getEmail(Long.toString(mThreadId)).names;
            String receiveEmail = RecipientIdCache.getEmail(Long.toString(mThreadId)).emails;
            String senderEmail = ConfigSettingPreferences.getInstance(mActivity).getPrefsUserEmail();

            mSendTask = new SendMessageTask(senderEmail, receiveEmail, receiverName, message);
            mSendTask.execute((Void) null);
        } else {
            Toast.makeText(mActivity, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public class SendMessageTask extends AsyncTask<Void, Void, Boolean> {

        private final String mSenderEmail;
        private final String mReceiverEmail;
        private final String mReceiverName;
        private final String mContentMessage;

        public SendMessageTask(String mSenderEmail, String mReceiverEmail, String mReceiverName, String mContentMessage) {
            this.mSenderEmail = mSenderEmail;
            this.mReceiverEmail = mReceiverEmail;
            this.mReceiverName = mReceiverName;
            this.mContentMessage = mContentMessage;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            MessageRequest request = new MessageRequest(mSenderEmail, mReceiverEmail, mContentMessage);

            try {
                CommonResponse response = mHttpClient.sendRequest("/api/messageRequest", MessageRequest.class, CommonResponse.class, request);

                if (response.getResultCode().equals("200")) {
                    // 메시지 전송 성공
                    result = true;
                } else if (response.getResultCode().equals("404")) {
                    // 메시지 전송 실패
                    result = false;
                } else {
                    // TODO :: 예외 에러코드 처리
                    result = false;
                }
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSendTask = null;

            if (success) {
                mInputMessage.setText("");
                mThreadId = MessageDB.getInstance().storeMessage(mActivity, CommonUtils.MESSAGE, mReceiverEmail,
                        mReceiverName, mContentMessage, MessageDB.SEND_TYPE);
            }

            Message msg = responseHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(RESPONSE_MSG, API_ID_MESSAGE_REQUEST);
            bundle.putBoolean("result", success);
            msg.setData(bundle);
            responseHandler.sendMessage(msg);
        }

        @Override
        protected void onCancelled() {
            mSendTask = null;
        }
    }


}
