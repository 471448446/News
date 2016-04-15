package better.news.ui.widget.dialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import better.news.R;
import better.news.support.util.Utils;
import better.news.ui.read.search.SearchBookActivity;

/**
 * Created by Better on 2016/4/15.
 */
public class SearchDialog extends BaseDialogView {
    @Override
    protected View getContentView() {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_search,null);
        view.findViewById(R.id.dialog_simple_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final EditText et= (EditText) view.findViewById(R.id.dialog_search_et);
        view.findViewById(R.id.dialog_simple_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=et.getText().toString().trim();
                if (TextUtils.isEmpty(str)){
                    Utils.toastShort(getActivity(),R.string.str_hint_book_name);
                }else{
                    SearchBookActivity.start(getActivity(),str);
                    dismiss();
                }
            }
        });
        ((TextView)view.findViewById(R.id.dialog_base_layout_tv_title)).setText(R.string.str_search);
        return view;
    }
}
