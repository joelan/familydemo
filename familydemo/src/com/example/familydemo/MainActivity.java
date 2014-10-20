package com.example.familydemo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	int clnumber;
	HashMap<String, circle> map;
	RelativeLayout rootview;
	int width;
	int height;
	circle center;
	private DisplayMetrics metric;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rootview = (RelativeLayout) findViewById(R.id.rootview);
		clnumber = 6;
		metric = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metric);
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels / 2;
		height = metric.heightPixels / 2;
		map = new HashMap<String, circle>();
		center = new circle();
		center.setP(new Point(width, height));
		center.setRadius(20);
		map.put(center.getP().x + "," + center.getP().y, center);
		
		Initpositondata();
		createcricles();
	

	}

	public void createcricles()
	{
		Iterator all = map.keySet().iterator();
		while(all.hasNext())
		{
			circle ccl= map.get(all.next());
	
			ImageView  view=new ImageView(this);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(this,ccl.getRadius())*2, DisplayUtil.dip2px(this,ccl.getRadius())*2);       
			
			layoutParams.topMargin=ccl.getP().y-DisplayUtil.dip2px(this,ccl.getRadius());      
			layoutParams.leftMargin=ccl.getP().x-DisplayUtil.dip2px(this,ccl.getRadius()); 
			view.setLayoutParams(layoutParams);
			view.setBackgroundResource(R.drawable.cricle);
		rootview.addView(view);			
		}
		
		
	}
	
	public void Initpositondata() {
		
		int sum = 0;
		Random rd = new Random(40);
		Random rx = new Random(width - 40);
		Random ry = new Random(height - 40);

		while (sum < clnumber) {

			int r = rd.nextInt();
			int x = rx.nextInt();
			int y = ry.nextInt();

			x = x +  DisplayUtil.dip2px(this, r);
			y = y +  DisplayUtil.dip2px(this, r);
			Point p1 = new Point(x, y);
			Boolean OK = true;
			Iterator iterator = map.keySet().iterator();
			while (iterator.hasNext()) {

				circle cl = map.get(iterator.next());
				Point p2 = cl.getP();

				double distant = Math.sqrt(Math.abs((p1.x - p2.x)
						* (p1.x - p2.x) + (p1.y) - p2.y)
						* (p1.y - p2.y));

				if (distant >  DisplayUtil.dip2px(this, r+cl.getRadius())) {
					double distanzhi = getLength(cl.getP().x, cl.getP().y,
							center.getP().x, center.getP().y, p1.x, p2.y);
					if (distanzhi >  DisplayUtil.dip2px(this, r)) {
						continue;
					} else {
						break;
					}

				} else {
					OK = false;
					break;
				}

			}
			if(OK==true)
			{
				circle cc=new circle();
				cc.setP(p1);
				cc.setRadius(r);
				map.put(p1.x+","+p1.y, cc);
				sum++;
				
			}
			

		}

	}

	public static Double getLength(double lx1, double ly1, double lx2,
			double ly2, double px, double py) {
		Double length = null;
		double b = getLenWithPoints(lx1, ly1, px, py);
		double c = getLenWithPoints(lx2, ly2, px, py);
		double a = getLenWithPoints(lx1, ly1, lx2, ly2);

		if (c + b == a) {// 点在线段上
			length = (double) 0;
		} else if (c * c >= a * a + b * b) { // 组成直角三角形或钝角三角形，投影在point1延长线上，
			length = b;
		} else if (b * b >= a * a + c * c) {// 组成直角三角形或钝角三角形，投影在point2延长线上，
			length = c;
		} else {
			// 组成锐角三角形，则求三角形的高
			double p = (a + b + c) / 2;// 半周长
			double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
			length = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
		}

		return length;
	}

	private static Double getLenWithPoints(double p1x, double p1y, double p2x,
			double p2y) {
		Double length = null;
		length = Math.sqrt(Math.pow(p2x - p1x, 2) + Math.pow(p2y - p1y, 2));
		return length;
	}

}
