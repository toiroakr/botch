package kom.botch.rasaki;

import com.example.mapdemo.R;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewHolder {
	// ImageView bgImg = null;
	// TextView budget = null;
	TextView title = null;
	RatingBar rate = null;

	ViewHolder(View base) {
		// this.bgImg = (ImageView) base.findViewById(R.id.side_backImg);
		// this.budget = (TextView) base.findViewById(R.id.side_budget);
		this.title = (TextView) base.findViewById(R.id.side_title);
		this.rate = (RatingBar) base.findViewById(R.id.side_rate);
	}
}
