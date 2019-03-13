package com.victordev.pokegroup.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.cardview.widget.CardView;
import com.victordev.pokegroup.R;


public class NightModeButton extends RelativeLayout {

    public NightModeButton(Context context) {
        super(context);
    }

    //Context
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    //Views
    protected ImageView switchIV;
    protected CardView switchRL;

    //Listener
    protected OnSwitchListener onSwitchListener;
    private boolean inAnimation=false;

    public NightModeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    boolean isNight=false;

    public void init(final Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        View rootView = mLayoutInflater.inflate(R.layout.night_mode_button_layout, this, true);
        switchRL = rootView.findViewById(R.id.switchRL);
        switchIV = rootView.findViewById(R.id.switchIV);


        switchRL.setOnClickListener(v -> {
           Animacion(true);
        });

    }

    public void Animacion(Boolean click){
        if(isNight && !inAnimation){
            isNight=false;
            inAnimation=true;
            ObjectAnimator
                    .ofFloat(switchIV, "rotation", 0,360)
                    .setDuration(400)
                    .start();
            ObjectAnimator
                    .ofFloat(switchIV, "translationX", switchRL.getWidth()/2, 0)
                    .setDuration(400)
                    .start();
            Handler handler = new Handler();
            handler.postDelayed(() -> switchIV.setImageDrawable(mContext.getDrawable(R.drawable.day_icon)),350);
            ValueAnimator valueAnimator = ValueAnimator.ofArgb(Color.parseColor("#353535"), Color.parseColor("#dadada"));
            valueAnimator.setDuration(400);
            valueAnimator.start();
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    inAnimation=false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            valueAnimator.addUpdateListener(animation -> switchRL.setCardBackgroundColor((int)animation.getAnimatedValue()));
            if(click){
                nightModeButtonClicked(isNight);
            }

        }else {

            if(!inAnimation) {
                isNight = true;
                ObjectAnimator
                        .ofFloat(switchIV, "rotation", 360, 0)
                        .setDuration(400)
                        .start();
                ObjectAnimator
                        .ofFloat(switchIV, "translationX", 0, switchRL.getWidth() / 2)
                        .setDuration(400)
                        .start();
                ValueAnimator valueAnimator = ValueAnimator.ofArgb(Color.parseColor("#dadada"), Color.parseColor("#353535"));
                valueAnimator.setDuration(400);
                valueAnimator.start();
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        inAnimation = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                valueAnimator.addUpdateListener(animation -> switchRL.setCardBackgroundColor((int) animation.getAnimatedValue()));
                switchIV.setImageDrawable(mContext.getDrawable(R.drawable.night_icon));

                if(click){
                    nightModeButtonClicked(isNight);
                }
            }
        }
    }

  public void setNight(Boolean night){
        isNight = !night;
        Animacion(false);
  }

    public interface OnSwitchListener{
        void onSwitchListener(boolean isNight);
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    public void nightModeButtonClicked(boolean isNight){
        if(onSwitchListener!=null){
            onSwitchListener.onSwitchListener(isNight);
        }
    }
}