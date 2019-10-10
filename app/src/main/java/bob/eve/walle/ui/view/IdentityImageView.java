package bob.eve.walle.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import bob.eve.walle.R;

/**
 * 1.尺寸的计算全部使用浮点，操作控件的时候在转为整型，来减少计算过程的数据失准
 * 2。对属性的添加和控制可以自行增加自定义属性和接口以便扩展结合自己的业务
 * 3。该类默认小圆图形的半径比圆形边框的宽度的一半大
 * 4。radiusScale的值小于等于0时 认为不显示小圆
 * 5。在小图片上的文字显示该类只是做了实现，需要适配和定制的可以做自己的修改
 * @author zhangmeng
 */
public class IdentityImageView extends ViewGroup {
    private Context mContext;
    private CircleImageView bigImageView;//大圆图
    private CircleImageView smallImageView;//小圆图
    private float radiusScale = 0;//小圆图片与大圆图片的比例，默认0.28
    private float bigRadius;//大圆图片半径
    private float smallRadius;//小圆图片半径
    private double angle = 0; //小圆图片的位置的角度大小
    private boolean isprogress;//是否可以加载进度条，必须设置为true才能开启
    private int progressCollor;//进度条颜色
    private int borderColor = 0;//边框颜色
    private int borderWidth = 0;//边框、进度条宽度
    private TextView textView;// 跟随小图片的提示文字，用的地方比较少
    private boolean hintSmallView;//是否隐藏小圆图片
    private Paint mBorderPaint;//边框画笔
    private Paint mProgressPaint;//进度条画笔
    private float progresss;// 进度条的进度值
    private Drawable bigImage;//大图片
    private Drawable smallimage;//小图片
    private int setprogressColor = 0;//动态设置进度条颜色值
    private int setborderColor = 0;//动态设置边框颜色值
    private float totalwidth;// 当前整体view的总的宽度值

    public IdentityImageView(Context context) {
        this(context, null);
    }

    public IdentityImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IdentityImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        // 在viewgroup初始化的时候，它调用了一个私有方法：initViewGroup，
        // 它里面会有一句setFlags（WILLL_NOT_DRAW,DRAW_MASK）;相当于调用了setWillNotDraw（true），
        // 所以说，对于ViewGroup，他就认为是透明的了，如果我们想要重写onDraw，就要调用setWillNotDraw（false）。
        setWillNotDraw(false);

        addThreeView();

        initAttrs(attrs);

        initPaint();

    }

    private void addThreeView() {


        bigImageView = new CircleImageView(mContext);//大圆图形

        smallImageView = new CircleImageView(mContext);//小圆图形

        textView = new TextView(mContext);// 中间提示的文本
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.holo_red_light));

        addView(bigImageView, 0, new LayoutParams((int) bigRadius, (int) bigRadius));

        addView(smallImageView, 1, new LayoutParams((int) smallRadius, (int) smallRadius));

        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // 以下两行代码保证小图片和文字可以被看到
        smallImageView.bringToFront();//使小图片位于最上层
        textView.bringToFront();//使小图片上面的文字显示位于小图片上面
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray tta = mContext.obtainStyledAttributes(attrs,R.styleable.IdentityImageView);
        bigImage = tta.getDrawable(R.styleable.IdentityImageView_iciv_bigimage);
        smallimage = tta.getDrawable(R.styleable.IdentityImageView_iciv_smallimage);
        angle = tta.getFloat(R.styleable.IdentityImageView_iciv_angle, 0);//小图以及进度条起始角度
        radiusScale = tta.getFloat(R.styleable.IdentityImageView_iciv_radiusscale, 0);//大图和小图的比例

        //是否要进度条，不为true的话，设置进度条颜色和宽度也没用
        isprogress = tta.getBoolean(R.styleable.IdentityImageView_iciv_isprogress, false);
        progressCollor = tta.getColor(R.styleable.IdentityImageView_iciv_progress_collor, 0);//边框进度条颜色

        borderColor = tta.getColor(R.styleable.IdentityImageView_iciv_border_color, 0);//边框颜色
        borderWidth = tta.getInteger(R.styleable.IdentityImageView_iciv_border_width, 0);//边框宽（同为进度条）
        hintSmallView = tta.getBoolean(R.styleable.IdentityImageView_iciv_hint_smallimageview, false);//隐藏小图片
        if (hintSmallView) {
            smallImageView.setVisibility(GONE);
            textView.setVisibility(GONE);
        }
        if (bigImage != null) {
            bigImageView.setImageDrawable(bigImage);
        }
        if (smallimage != null) {
            smallImageView.setImageDrawable(smallimage);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int viewHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int viewWidht = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        switch (viewWidthMode) {
            case MeasureSpec.EXACTLY: // 说明在布局文件中使用的是具体值：100dp或者match_parent
                // 为了方便，让半径等于宽高中小的那个，再设置宽高至半径大小
                totalwidth = viewWidht < viewHeight ? viewWidht : viewHeight;

                // 用大图和小图的大小比例以及圆形边宽来计算大图的半径
                if (radiusScale > 0) {
                    float scale = 1 + radiusScale;

                    // 在该情况下大圆和小圆的半径加起来等于宽度一半减去圆形边款的一半
                    float radius2 = totalwidth / 2 - borderWidth / 2;
                    bigRadius = radius2 / scale;
                } else {
                    // 在该情况下大圆和小圆的半径加起来等于宽度一半减去圆形边款的宽度
                    bigRadius = totalwidth / 2 - borderWidth;
                }

                break;
            default: // 在其他情况我们写死默认的宽高,可以随意修改
                bigRadius = 200;
                totalwidth = (int) ((bigRadius + bigRadius * radiusScale + borderWidth) * 2);
                break;
        }
        setMeasuredDimension((int) totalwidth, (int) totalwidth);
        adjustThreeView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();

        if (borderWidth > 0) {
            drawBorder(canvas);
        }
        if (isprogress && borderWidth > 0) {
            drawProgress(canvas);
        }


    }

    private void initPaint() {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setAntiAlias(true);
        }
        if (setborderColor != 0) {
            mBorderPaint.setColor(getResources().getColor(setborderColor));
        } else {
            mBorderPaint.setColor(borderColor);
        }
        mBorderPaint.setStrokeWidth(borderWidth);

        if (mProgressPaint == null) {
            mProgressPaint = new Paint();
            mProgressPaint.setStyle(Paint.Style.STROKE);
            mProgressPaint.setAntiAlias(true);
        }
        if (setprogressColor != 0) {
            mProgressPaint.setColor(getResources().getColor(setprogressColor));
        } else {
            mProgressPaint.setColor(progressCollor);
        }

        mProgressPaint.setStrokeWidth(borderWidth);

    }

    /**
     * 画圆形的边框
     *
     * @param canvas 画布
     */
    private void drawBorder(Canvas canvas) {

        // 在于setStrokeWidth这个方法，圆环宽度是往外侧增加一半，往内侧增加一半
        // 所以画笔的中心到圆心的距离才为圆的真正画出的半径
        if (radiusScale > 0) {
            // 圆形边框的半径等于宽度一半减去小圆半径
            canvas.drawCircle(totalwidth / 2, totalwidth / 2, totalwidth / 2 - smallRadius, mBorderPaint);
        } else {
            // 圆形边框的半径等于宽度一半减去圆形边款宽度的一半
            canvas.drawCircle(totalwidth / 2, totalwidth / 2, (totalwidth - borderWidth) / 2, mBorderPaint);
        }
    }

    //画圆弧进度条
    private void drawProgress(Canvas canvas) {

        // 在于setStrokeWidth这个方法，圆环宽度是往外侧增加一半，往内侧增加一半
        // 所以画笔的中心到圆心的距离才为圆的真正画出的半径
        // 这里在layout里面小圆的中心点正好在圆形边的中心所以直接用宽高减去小圆的半径就是进度条画笔的中心，
        // 一样的画笔宽度就可以覆盖圆形边的宽度
        RectF rectf;
        if (radiusScale > 0) {
            // 有小圆时，半径为到小圆的中心点
            rectf = new RectF(smallRadius, smallRadius,
                    getWidth() - smallRadius, getHeight() - smallRadius);
        } else {
            // 没小圆时，半径为到圆形边框宽度的中点
            rectf = new RectF(borderWidth / 2, borderWidth / 2,
                    getWidth() - borderWidth / 2, getHeight() - borderWidth / 2);
        }
        canvas.drawArc(rectf, (float) angle, progresss, false, mProgressPaint);
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //重点在于smallImageView的位置确定,默认为放在右下角，可自行拓展至其他位置
        double cos = Math.cos(angle * Math.PI / 180);
        double sin = Math.sin(angle * Math.PI / 180);

        if (radiusScale > 0) {
            // 大圆的半径加上圆形边宽的一半才是可以用来通过函数计算小圆中心位置的变数 (bigRadius + borderWidth / 2)
            double left = totalwidth / 2 + (bigRadius + borderWidth / 2) * cos - smallRadius;
            double top = totalwidth / 2 + (bigRadius + borderWidth / 2) * sin - smallRadius;

            int right = (int) (left + 2 * smallRadius);
            int bottom = (int) (top + 2 * smallRadius);

            textView.layout((int) left, (int) top, right, bottom);
            smallImageView.layout((int) left, (int) top, right, bottom);

            bigImageView.layout((int) (smallRadius + borderWidth / 2), (int) (smallRadius + borderWidth / 2),
                    (int) (totalwidth - smallRadius - borderWidth / 2), (int) (totalwidth - smallRadius - borderWidth / 2));
        } else {
            bigImageView.layout((int) borderWidth, (int) borderWidth,
                    (int) (totalwidth - borderWidth), (int) (totalwidth - borderWidth));
        }

    }

    private void adjustThreeView() {
        bigImageView.setLayoutParams(new LayoutParams((int) (bigRadius * 2), (int) (bigRadius * 2)));
        if (radiusScale > 0) {
            smallRadius = (int) (bigRadius * radiusScale);
            smallImageView.setLayoutParams(new LayoutParams((int) (smallRadius * 2), (int) (smallRadius * 2)));
            textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            smallImageView.setVisibility(VISIBLE);
            textView.setVisibility(VISIBLE);
        } else {
            smallImageView.setVisibility(GONE);
            textView.setVisibility(GONE);
        }
    }

    /**
     * 获得textview
     *
     * @return textView
     */
    public TextView getTextView() {
        if (textView != null) {
            return textView;
        } else {
            return null;
        }
    }

    /**
     * 获得大图片imageview
     *
     * @return bigImageView
     */
    public CircleImageView getBigCircleImageView() {
        if (bigImageView != null) {
            return bigImageView;
        } else {
            return null;
        }
    }

    /**
     * 获得小图片imageview
     *
     * @return smallImageView
     */
    public CircleImageView getSmallCircleImageView() {
        if (smallImageView != null) {
            return smallImageView;
        } else {
            return null;
        }
    }

    /**
     * 设置进度条进度，一共360
     *
     * @param angle 进度大小
     */
    public void setProgress(float angle) {
        if (progresss == angle || angle < 0 || angle > 360) {
            return;
        }
        progresss = angle;
        textView.setText((int) angle+"");
        requestLayout();
        invalidate();
    }

    /**
     * 设置小图片的的位置的角度和进度条的起始位置角度
     *
     * @param angles 角度
     */
    public void setAngle(int angles) {
        if (angles == angle) {
            return;
        }
        angle = angles;
        requestLayout();
        invalidate();
    }

    /**
     * 设置小圆和大圆的比例值
     *
     * @param v 比例
     */
    public void setRadiusScale(float v) {
        if (v == radiusScale || v < 0 || v > 1) {
            return;
        }
        radiusScale = v;
        requestLayout();
        invalidate();

    }

    /**
     * 设置是否可以显示进度条
     *
     * @param b 是否有进度条
     */
    public void setIsprogress(boolean b) {

        if (b == isprogress) {
            return;
        }
        isprogress = b;
        requestLayout();
        invalidate();
    }

    /**
     * 设置填充的颜色
     *
     * @param color 边框颜色
     */
    public void setBorderColor(int color) {
        if (color == borderColor) {
            return;
        }
        setborderColor = color;
        requestLayout();
        invalidate();

    }

    /**
     * 设置进度条颜色
     *
     * @param color 进度条颜色
     */
    public void setProgressColor(int color) {
        if (color == progressCollor) {
            return;
        }
        setprogressColor = color;
        requestLayout();
        invalidate();
    }

    /**
     * 设置边框和进度条的宽度
     *
     * @param width 边框和进度条宽度
     */
    public void setBorderWidth(int width) {
        if (width == borderWidth) {
            return;
        }
        borderWidth = width;
        requestLayout();
        invalidate();
    }
}
