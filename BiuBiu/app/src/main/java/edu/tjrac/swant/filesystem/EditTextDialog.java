package edu.tjrac.swant.filesystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.EditText;

/**
 * Created by wpc on 2017/6/19.
 */
@SuppressLint({"NewApi", "ValidFragment"})
public class EditTextDialog extends DialogFragment {
    EditText editText;

    public EditTextDialog(Context context, String title, String hint) {
        mContext = context;
        this.title = title;
        this.hint = hint;
    }

    Context mContext;
    String title;
    String hint;
    DialogInterface.OnClickListener positive;

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public DialogInterface.OnClickListener getPositive() {
        return positive;
    }

    public EditTextDialog setPositive(DialogInterface.OnClickListener positive) {
        this.positive = positive;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        int dp18 = UiUtils.Dp2Px(mContext, 18);
//        params.setMargins(dp18, 0, dp18, 0);
//        editText.setLayoutParams(params);
//        editText.setPadding(dp18,0,dp18,0);
//        View v = LayoutInflater.from(mContext).inflate(R.layout.named_dialog, null);
//        editText = (EditText) v.findViewById(R.id.et_named_dialog);
        editText=new EditText(mContext);
        editText.setHint(hint);
        return new AlertDialog.Builder(mContext).setTitle(title)
                .setView(editText)
                .setPositiveButton("确定", positive).create();
    }
}