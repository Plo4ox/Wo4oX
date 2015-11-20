
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.List;

/** Based on https://gist.github.com/Antarix/6388606 :: TypeWriter */

public class TypeWriter extends TextView {
    private Context             mContext;
    private WritingText         mText;
    private List<WritingText>   mArrayText;
    private int                 mIndex;
    private int                 mArrayIndex;
    private long                mDelay      = 115;
    private long                mArrayDelay = 300;

    public TypeWriter(Context context) {
        super(context);
        mContext = context;
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            int len =  mText.str.length();

            if (mIndex <= len)
                setText(mText.str.subSequence(0, mIndex));
            if (++mIndex <= len)
                mHandler.postDelayed(characterAdder, mDelay);
            else if (mText != null && mText.fct != null
                    && mText.actionMoment == WritingText.AFTER)
                mText.fct.writingAction(mContext);
            if (mIndex > len) {
                if (mIndex == len + 1)
                    mArrayIndex++;
                mArrayHandler.postDelayed(stringArrayAdder, mArrayDelay);
            }
        }
    };

    private Handler mArrayHandler = new Handler();
    private Runnable stringArrayAdder = new Runnable() {
        @Override
        public void run() {
            if (mArrayText != null && mArrayIndex < mArrayText.size()) {
                WritingText text = mArrayText.get(mArrayIndex);
                if (text.fct != null
                        && text.actionMoment == WritingText.BEFORE)
                    text.fct.writingAction(mContext);
                animateWritingText(text);
            }
        }
    };

    public void pause() {
        mArrayHandler.removeCallbacks(stringArrayAdder);
        mHandler.removeCallbacks(characterAdder);
    }
    public void restart() {
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void animateWritingText(WritingText text) {
        mText = text;
        mIndex = 0;
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }
    public void animateWritingList(List<WritingText> texts) {
        mArrayText = texts;
        mArrayIndex = 0;
        mArrayHandler.removeCallbacks(stringArrayAdder);
        mArrayHandler.postDelayed(stringArrayAdder, mArrayDelay);
    }
    public void setCharacterDelay(long millis) { mDelay = millis; }
    public void setStringDelay(long millis) { mArrayDelay = millis; }
}
