package net.simonvt.datepicker;

import java.util.Calendar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 *   这是自定义的部分，请在这里修改布局等
 *
 */
public class DatePickDialog extends Dialog implements View.OnClickListener,
		DatePicker.OnDateChangedListener {
	private static final String tag = DatePickDialog.class.getSimpleName();
	
	private final Context mContext;
	private TextView tvTitle;
	private Button btn_sure;
	private CharSequence positiveText;
	private CharSequence title;
	private Button btn_cancel;
	private CharSequence negativeText;
	private Calendar mCalendar;

	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";

	private int year;
	private int monthOfYear;
	private int dayOfMonth;

	DatePicker mDatePicker;

	private boolean mTitleNeedsUpdate = true;

	public static String date = null;
	
	/**
	 * 
	 * @param context
	 * @param title   标题
	 * @param positiveText   确定
	 * @param negativeText	 取消
	 */
	public DatePickDialog(Context context,CharSequence title, CharSequence positiveText,CharSequence negativeText) {
		super(context);
		this.mContext = context;
		this.title = title;
		this.positiveText = positiveText;
		this.negativeText = negativeText;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_layout);

		initView();
	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(title);

		btn_sure = (Button) findViewById(R.id.btn_datetime_sure);
		if (positiveText != null) {
			btn_sure.setVisibility(View.VISIBLE);
			btn_sure.setText(positiveText);
		}
		btn_cancel = (Button) findViewById(R.id.btn_datetime_cancel);
		if (negativeText != null) {
			btn_cancel.setVisibility(View.VISIBLE);
			btn_cancel.setText(negativeText);
		}

		btn_cancel.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		
		mCalendar = Calendar.getInstance();
		year = mCalendar.get(Calendar.YEAR);
		monthOfYear = mCalendar.get(Calendar.MONTH);
		dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

		mDatePicker = (DatePicker) findViewById(R.id.datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		mDatePicker.init(year, month, day, this);
	}

	@Override
	public void onClick(View v) {
		if(v==btn_cancel){
			this.dismiss();
		}else if(v == btn_sure){
			date = mDatePicker.getYear()+"-"+mDatePicker.getMonth()+"-"+mDatePicker.getDayOfMonth();
			this.dismiss();
		}
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int year = savedInstanceState.getInt(YEAR);
		int month = savedInstanceState.getInt(MONTH);
		int day = savedInstanceState.getInt(DAY);
		mDatePicker.init(year, month, day, this);
	}

}
