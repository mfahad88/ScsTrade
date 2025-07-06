package com.example.scstrade.views.widgets;

import static androidx.core.content.ContextCompat.startActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import com.example.scstrade.R;
import com.example.scstrade.databinding.CustomToolbarBinding;
import com.example.scstrade.databinding.MmarketBinding;
import com.example.scstrade.viewmodels.SharedViewModel;
import com.example.scstrade.views.MyApp;
import com.example.scstrade.views.aof.AofActivity;
import com.example.scstrade.views.notification.NotificationActivity;
import com.example.scstrade.views.search.SearchActivity;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class mMarket extends LinearLayout {
    public CustomToolbarBinding binding;
    public ConstraintLayout contentLogo,contentText;
    HorizontalScrollView tickerScroll;
    LinearLayout tickerContainer;
    private int scrollSpeed = 2; // lower = faster
    private Handler handler = new Handler();
    private int scrollX = 0;
    private Runnable scrollRunnable;
    public mMarket(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public mMarket(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding= CustomToolbarBinding.inflate(LayoutInflater.from(context),this,true);
        Log.e("LifecycleOwner", "Context class: " + context.getClass().getName()+" "+(context instanceof AppCompatActivity));

       /* tickerScroll = binding.tickerScroll;

        tickerContainer = binding.tickerContainer;

        List<TickerItem> items = Arrays.asList(
                new TickerItem.Text("www.scstrade.com"),
                new TickerItem.Image(R.drawable.logo_splash),
                new TickerItem.Text("+92 3143966681"),
                new TickerItem.Image(R.drawable.logo_splash),
                new TickerItem.Text("TREC Holder Pakstan Stock Exchange Limited"),
                new TickerItem.Image(R.drawable.logo_splash)
        );
        ticker(items);*/


        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.mMarket,
                    0,0
            );
            try{
                Window window=((Activity) context).getWindow();
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                );
                SharedViewModel sharedViewModel=((MyApp) context.getApplicationContext()).viewModel;
                LifecycleOwner lifecycleOwner =  (LifecycleOwner) context;
                if(getActivity(context)!=null){
                    Log.e("Activity",((Activity)context).getClass().getSimpleName());
//                    binding.subTitle.setText(((Activity)context).getClass().getSimpleName().replace("Activity","").replace("Detail",""));
                   /* if(((Activity)context).getClass().getSimpleName().equals("MainActivity")){
                        binding.group.setVisibility(View.VISIBLE);

                        binding.titleItem.setVisibility(View.GONE);

                    }else{
                        binding.titleItem.setVisibility(View.VISIBLE);
                        binding.group.setVisibility(View.GONE);
                        binding.titleItem.setText(((Activity)context).getClass().getSimpleName().replace("Activity","").replace("Detail",""));

                    }*/

                }
                binding.aofTop.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getContext(),"Working In Progress under fixes",Toast.LENGTH_SHORT).show();
//                    .
                    context.startActivity(new Intent(context, AofActivity.class));
                    }
                });
                sharedViewModel.getMutableIndices().observe(lifecycleOwner, listResource -> {
                    if(listResource.getData()!=null){
                        if(listResource.getData().get(0).getMarketStatus().equalsIgnoreCase("close")){
                            binding.close.setVisibility(VISIBLE);
                            binding.open.setVisibility(GONE);
                            binding.closeMarket.setVisibility(VISIBLE);
                            binding.openMarket.setVisibility(GONE);
                        }else {
                            binding.close.setVisibility(GONE);
                            binding.open.setVisibility(VISIBLE);

                            binding.closeMarket.setVisibility(GONE);
                            binding.openMarket.setVisibility(VISIBLE);
                        }
                        SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yyyy | hh:mma", Locale.ENGLISH);
                        binding.dateTime.setText(sdf.format(new Date()));

                        SimpleDateFormat sdf1= new SimpleDateFormat("hh:mma", Locale.ENGLISH);
                        binding.dateTimeText.setText(sdf1.format(new Date()));
                    }
                });
//                binding.notificationIcon.setOnClickListener(view ->context.startActivity(new Intent(context, NotificationActivity.class)));
//                binding.searchIcon.setOnClickListener(view -> {context.startActivity(new Intent(context, SearchActivity.class));});

                binding.notification.setOnClickListener(view ->context.startActivity(new Intent(context, NotificationActivity.class)));
                binding.search.setOnClickListener(view -> {context.startActivity(new Intent(context, SearchActivity.class));});


                if(Build.VERSION.SDK_INT>=29){
                    ViewCompat.setOnApplyWindowInsetsListener(binding.contentText, new androidx.core.view.OnApplyWindowInsetsListener() {
                        @NonNull
                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(@NonNull View view, @NonNull WindowInsetsCompat windowInsets) {
                            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout());
                            view.setPadding(15,insets.top,50,insets.bottom);

                            return windowInsets;
                        }
                    }) ;
                }
               /* binding.textView7.setOnClickListener(view -> context.startActivity(new Intent(context, SearchActivity.class)));
                if(binding.contentLogo.getVisibility()==View.VISIBLE){
                    ViewCompat.setOnApplyWindowInsetsListener(binding.contentLogo, new androidx.core.view.OnApplyWindowInsetsListener() {
                        @NonNull
                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(@NonNull View view, @NonNull WindowInsetsCompat windowInsets) {
                            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout());
                            view.setPadding(15,insets.top,50,insets.bottom);

                            return windowInsets;
                        }
                    }) ;
                }else{
                    ViewCompat.setOnApplyWindowInsetsListener(binding.contentText, new androidx.core.view.OnApplyWindowInsetsListener() {
                        @NonNull
                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(@NonNull View view, @NonNull WindowInsetsCompat windowInsets) {
                            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout());
                            view.setPadding(15,insets.top,50,insets.bottom);

                            return windowInsets;
                        }
                    }) ;
                }

                contentLogo=binding.contentLogo;
                contentText = binding.contentText;*/

                binding.scstrade.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "https://scstrade.com/";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getContext().startActivity(intent);
                    }
                });
                binding.phone.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://wa.me/+923218296919" )); // Open chat with this number

                        try {
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                binding.imageViewWhatsapp.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://wa.me/+923218296919" )); // Open chat with this number

                        try {
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (Exception e){
                Log.e("LifecycleOwner",e.getMessage());
            }
            finally {
                a.recycle();
            }
        }


    }

  /*  public void toggleToolbar(boolean isHome){
        if(isHome){
            binding.contentLogo.setVisibility(View.VISIBLE);
            binding.contentText.setVisibility(View.GONE);
            binding.constraintMarketStat.setVisibility(View.VISIBLE);
            binding.tickerScroll.setVisibility(View.GONE);
        }else{
            binding.contentLogo.setVisibility(View.GONE);
            binding.contentText.setVisibility(View.VISIBLE);
            binding.constraintMarketStat.setVisibility(View.GONE);
            binding.tickerScroll.setVisibility(View.VISIBLE);
            startAutoScroll();
        }
    }*/

    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    public void ticker(List<TickerItem> items){

        for (TickerItem item : items) {
            View view = item.toView(getContext());
            tickerContainer.addView(view);

            // Optional spacing
            View space = new View(getContext());
            space.setLayoutParams(new LinearLayout.LayoutParams(40, 1));
            tickerContainer.addView(space);
        }
        tickerScroll.post(() -> {
            int totalWidth = tickerContainer.getWidth();
            ObjectAnimator animator = ObjectAnimator.ofInt(tickerScroll, "scrollX", 0, totalWidth / 2);
            animator.setDuration(30000); // 30 seconds
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new AccelerateInterpolator()); // linear
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.start();
        });
    }

    private void addTickerItems() {
        for (int i = 0; i < 10; i++) {
            TextView tv = new TextView(getContext());
            tv.setText("Item " + (i + 1) + "    ");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(18f);
            tickerContainer.addView(tv);
        }
    }


    private void duplicateContent() {
        int count = tickerContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View original = tickerContainer.getChildAt(i);
            View copy = duplicateView(original);
            tickerContainer.addView(copy);
        }
    }
    private View duplicateView(View view) {
        if (view instanceof TextView) {
            TextView orig = (TextView) view;
            TextView copy = new TextView(getContext());
            copy.setText(orig.getText());
            copy.setTextColor(orig.getCurrentTextColor());
            copy.setTextSize(TypedValue.COMPLEX_UNIT_PX, orig.getTextSize());
            return copy;
        }
        return new View(getContext()); // fallback
    }

    public void startAutoScroll() {
        tickerContainer.post(() -> {
            int width = tickerContainer.getWidth() / 2;

            ObjectAnimator animator = ObjectAnimator.ofFloat(
                    tickerContainer, "translationX", 0, -width
            );
            animator.setDuration(15000); // adjust speed here
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.start();
        });
        /*scrollRunnable = new Runnable() {
            @Override
            public void run() {
                scrollX += scrollSpeed;
                tickerScroll.scrollTo(scrollX, 0);
                int width = tickerContainer.getWidth() / 2;
                if (scrollX >= width) {
                    scrollX = 0;
                    tickerScroll.scrollTo(0, 0);
                }
                handler.postDelayed(this, 16); // ~60fps
            }
        };
        handler.post(scrollRunnable);*/
    }


}
