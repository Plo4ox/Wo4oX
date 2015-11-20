
import android.content.Context;

interface IWritingAction {
    void writingAction(Context c);
}

public class WritingText {
    public String           str;
    public IWritingAction   fct;
    public int              actionMoment;
    public static int       BEFORE  = -1;
    public static int       AFTER   = -2;

    WritingText(String s, IWritingAction action, Integer moment) {
        str = s;
        fct = action;
        actionMoment = moment;
    }
}

