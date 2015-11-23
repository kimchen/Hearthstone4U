package st.kimsmik.guidehearthstone4u;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kim on 2015/9/25.
 */
public class SubFilter extends LinearLayout {
    public SubFilter(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_filter, this);
    }
    public void init(String name,List<String> spinnerList,AdapterView.OnItemSelectedListener listener){
        TextView tv =  (TextView)findViewById(R.id.filterName);
        tv.setText(name);

        Spinner spinner = (Spinner)findViewById(R.id.filterSpinner);
        ArrayAdapter sa = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item, spinnerList);
        spinner.setAdapter(sa);

        spinner.setOnItemSelectedListener(listener);
    }
}
