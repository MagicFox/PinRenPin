package com.fox.pinrenpin;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.pinrenpin.view.AddScoreButton;
import com.fox.pinrenpin.view.CustomDialog;
import com.fox.pinrenpin.view.DigitalImageEdittText;
import com.fox.pinrenpin.view.GoButton;
import com.fox.pinrenpin.view.LabaCoinRewardDialog;
import com.fox.pinrenpin.view.LabaProgressBarView;
import com.fox.pinrenpin.view.PlaneView;
import com.fox.pinrenpin.view.ReduceScoreButton;
import com.fox.pinrenpin.view.SubscriptView;
import com.fox.pinrenpin.view.WheelView;
import com.fox.pinrenpin.view.WinScoreView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView ivBack;
    private ImageView ivSound;
    private ImageView ivLogo;
    private WinScoreView winScore;
    private PlaneView plane;
    private WheelView wheel;
    private TextView tvNewlabaMytoast;
    private RelativeLayout llProcessBar;
    private ImageView ivXp;
    private ImageView ivCoin;
    private RelativeLayout llInput;
    private LinearLayout llBalance;
    private TextView balance;
    private DigitalImageEdittText ivBets;
    private ReduceScoreButton reduceScore;
    private AddScoreButton addScore;
    private ImageView maxBets;
    private ImageView history;
    private SubscriptView subscript;
    private GoButton go;
    private ImageView gameRule;

    private void initViews() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivSound = (ImageView) findViewById(R.id.ivSound);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        winScore = (WinScoreView) findViewById(R.id.winScore);
        plane = (PlaneView) findViewById(R.id.plane);
        wheel = (WheelView) findViewById(R.id.wheel);
        tvNewlabaMytoast = (TextView) findViewById(R.id.tv_newlaba_mytoast);
        llProcessBar = (RelativeLayout) findViewById(R.id.llProcessBar);
        ivXp = (ImageView) findViewById(R.id.ivXp);
        ivCoin = (ImageView) findViewById(R.id.ivCoin);
        llInput = (RelativeLayout) findViewById(R.id.llInput);
        llBalance = (LinearLayout) findViewById(R.id.llBalance);
        balance = (TextView) findViewById(R.id.balance);
        ivBets = (DigitalImageEdittText) findViewById(R.id.bets);
        reduceScore = (ReduceScoreButton) findViewById(R.id.reduceScore);
        addScore = (AddScoreButton) findViewById(R.id.addScore);
        maxBets = (ImageView) findViewById(R.id.maxBets);
        history = (ImageView) findViewById(R.id.history);
        subscript = (SubscriptView) findViewById(R.id.subscript);
        go = (GoButton) findViewById(R.id.go);
        gameRule = (ImageView) findViewById(R.id.game_rule);
        progressBar = (LabaProgressBarView) findViewById(R.id.progressBar);
    }


    /** 超时 */
    private Runnable overTime;
    private Handler handler;
    /** 音效开关 */
    private boolean isSoundOn = true;
    /** 游戏是否进行中 */
    private boolean isGamePlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        initViews();
        init();
        process();
    }

    public static final int WHEELRESULT_NO = 0;
    public static final int WHEELRESULT_SHIWU = 1;
    public static final int WHEELRESULT_100 = 2;
    public static final int WHEELRESULT_10 = 3;
    public static final int WHEELRESULT_2 = 4;

    public static final int REQUEST_WIN = 100;
    public static final int RESPONSE_WIN = 101;
    public static final String TWO_WIN = "02";
    public static final String TEN_WIN = "010";
    public static final String HUNDER_WIN = "0100";
    boolean isOverTime = false;
    /** 当前请求标识 */
    private String currentRequestCustString = "";
    String resultMsg = "";
    private String earnPoints = "0";
    private boolean isArriveEarnPoint = false;
    private void init() {
        handler = new Handler();

        ivBack.setOnClickListener(onClickListener);

        ivSound.setOnClickListener(onClickListener);
        isSoundOn = MyPreference.getInstance().getLabaSound();
        ivSound.setImageResource(isSoundOn ? R.drawable.laba_sound_on_selector : R.drawable.laba_sound_off_selector);

//        //LOGO
        AnimationDrawable drawable = (AnimationDrawable) ivLogo.getDrawable();
        drawable.start();

        //飞机
        plane.setOnClickListener(onClickListener);

        ivBets.addTextChangedListener(watcher);

        reduceScore.setOnReduceTriggerListener(new ReduceScoreButton.OnReduceTriggerListener() {
            @Override
            public void onReduce() {
                updateBets(bets - ATOM_SCORE);
                playSounds(2, 0);
            }
        });

        addScore.setOnAddTriggerListener(new AddScoreButton.OnAddTriggerListener() {
            @Override
            public void onAdd() {
                updateBets(bets + ATOM_SCORE);
                playSounds(2, 0);
            }
        });

        maxBets.setOnClickListener(onClickListener);
        gameRule.setOnClickListener(onClickListener);
        history.setOnClickListener(onClickListener);

        overTime = new Runnable() {
            @Override
            public void run() {
                if(isGamePlaying){
                    isOverTime = true;
                    currentRequestCustString = "";
                    if(TextUtils.isEmpty(resultMsg)){
                        resultMsg = newlaba_overtime;
                    }
                    setLabaRecordOutTime();
                    wheelStopDelay(WHEELRESULT_NO);
                }
            }
        };
        initRunnable();

        go.setEnabled(true);
        go.setOnClickListener(onClickListener);
        go.setOnGoClickListener(new GoButton.OnGoClickListener() {
            @Override
            public void onClick() {
                earnPoints = "0";
                isOverTime = false;


                playSounds(1, 0);
                if (!isGamePlaying) {
                    int tempResult = checkBets(ivBets.getText().toString(), balance.getText().toString());
                    switch (tempResult) {
                        case 0:
                            initRunnable();
                            go.postDelayed(overTime, 15000);
                            bets = Integer.parseInt(ivBets.getText().toString());
                            isGamePlaying = true;
                            changEditTextState(false);
                            wheel.start();
                            isArriveEarnPoint = false;
                            currentRequestCustString = "android" + System.currentTimeMillis();
                            String uuId = IdGenerator.create();
                            record.setuuId(uuId);
                            run();
                            break;
                        case 1:
                            // 输入不合法
                            upGoButtom();
                            resultMsg = getString(R.string.newlaba_beterror_illegal);
                            showToastWithOutGame();
                            break;
                        case 2:
                            // 分值过小
                            upGoButtom();
                            resultMsg = getString(R.string.newlaba_beterror_toosmall);
                            showToastWithOutGame();
                            break;
                        case 3:
                            // 分值过大
                            upGoButtom();
                            resultMsg = getString(R.string.newlaba_beterror_toobig);
                            showToastWithOutGame();
                            break;
                        case 4:
                            // 分值不是50的倍数
                            upGoButtom();
                            resultMsg = getString(R.string.newlaba_beterror_50times);
                            showToastWithOutGame();
                            break;
                        case 5:
                            // 分值余额不足
                            upGoButtom();
                            resultMsg = getString(R.string.newlaba_beterror_notenough);
                            showToastWithOutGame();
                            break;
                        default:
                            upGoButtom();
                            break;
                    }
                } else {
                    // 游戏正在运行中
                }

            }
        });

        wheel.setOnStateChangedListener1(new WheelView.OnStateChangedListener() {
            @Override
            public void onStop() {
                playSounds(4, 0);
            }

            @Override
            public void onTriggerStopSound() {

            }
        });

        wheel.setOnStateChangedListener2(new WheelView.OnStateChangedListener() {
            @Override
            public void onStop() {

            }

            @Override
            public void onTriggerStopSound() {
                playSounds(4, 0);
            }
        });
        wheel.setOnStateChangedListener(new WheelView.OnStateChangedListener() {
            @Override
            public void onStop() {
                upGoButtom();
                updateUserScore();
                isGamePlaying = false;
                go.removeCallbacks(overTime);
                changEditTextState(true);
                try {
                    Long earnPointLong = Long.valueOf(earnPoints);
                    if (earnPointLong > 0) {
                        isArriveEarnPoint = true;
                        showGivePointDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                showToast();
                // changeScore(winScoreThisTime);
                upGoButtom();
                if (!isArriveEarnPoint) {
                    goToWin();
                }
            }

            @Override
            public void onTriggerStopSound() {
                playSounds(4, 0);
            }
        });


        subscript.setCount(MyPreference.getInstance().getLabaHistoryCount());

    }

    private void showToast() {
        if (!TextUtils.isEmpty(resultMsg) && !isWin) {
            handler.post(runnableToast);
        }
    }
    private void showGivePointDialog() {
        handler.post(showGiveDialogRunnable);
    }

    LabaCoinRewardDialog labaCoinRewardDialog;
    Runnable showGiveDialogRunnable = new Runnable() {

        @Override
        public void run() {
            if (labaCoinRewardDialog == null) {
                labaCoinRewardDialog = new LabaCoinRewardDialog(MainActivity.this, R.layout.dialog_laba_reward);
            }
            labaCoinRewardDialog.setOnGivePointBtnClickListener(onGivePointBtnClickListener);
            labaCoinRewardDialog.setUIDataAndShow(earnPoints);
        }
    };

    LabaCoinRewardDialog.OnGivePointBtnClickListener onGivePointBtnClickListener = new LabaCoinRewardDialog.OnGivePointBtnClickListener(){

        @Override
        public void GivePointBtnClick(String earnPoints) {
            tempScore = String.valueOf(customPoints);
            updateUserScore();
            if (isArriveEarnPoint) {
                goToWin();
            }
        }
    };

    /**
     * 跳转登录页
     */
    protected void goToWin() {
//        if (isWin && isWinByWinBitmapList(winBitmapList)) {
//            if (winNumber == 2 || winNumber == 10) {
//                playSounds(6, 0);
//            } else {
//                playSounds(7, 0);
//            }
//            int centerH = findViewById(R.id.rl_labanew_top).getHeight() + wheel.getHeight() / 2;
//            boolean isSwitchOpen = isWinIdSwitchOpen(winID);
//            Intent intent = new Intent();
//            intent.setClass(this, LabaNewWinActivity.class);
//            intent.putExtra("isShiWuWin", isShiWuWin);
//            intent.putExtra("winID", winID);
//            intent.putExtra("winName", winName);
//            intent.putExtra("winGameCash", winGameCash);
//            intent.putExtra("winNumber", winNumber);
//            intent.putExtra("winBitmapList", winBitmapList);
//            intent.putExtra("win2Fruit", win2Fruit);
//            intent.putExtra("centerH", centerH);
//            intent.putExtra("winIDSwitch", isSwitchOpen);
//            startActivityForResult(intent, REQUEST_WIN);
//        } else {
//            playSounds(5, 0);
//        }

        playSounds(5, 0);
        isWin = false;
        isShiWuWin = false;
        winID = "";
        winName = "";
        winGameCash = "";
        winNumber = 1;
        winBitmapList = new int[] { -1, -1, -1 };
        win2Fruit = -1;
    }

    private String tempScore = "";
    private void updateUserScore() {
        if (!TextUtils.isEmpty(tempScore)) {

            balance.postDelayed(new Runnable() {
                @Override
                public void run() {
                    balance.setText(tempScore);
                    tempScore = "";
                }
            }, 100);
        }
    }
    Runnable runnableToast = new Runnable() {
        @Override
        public void run() {
            // 更新界面
            if (!TextUtils.isEmpty(resultMsg)) {

                tvNewlabaMytoast.setText(resultMsg);
                tvNewlabaMytoast.setVisibility(View.VISIBLE);
                tvNewlabaMytoast.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvNewlabaMytoast.setVisibility(View.GONE);
                        // fix bug:modify by lgw
                        // resultMsg = "";
                    }
                }, 2000);
            }
        }
    };
    private void showToastWithOutGame() {
        if (!TextUtils.isEmpty(resultMsg)) {
            handler.post(runnableToast);
        }
    }
    private void upGoButtom() {
        go.postDelayed(new Runnable() {
            @Override
            public void run() {
                go.setEnabled(true);
            }
        }, 100);
    }
    String newlaba_overtime = "网络不给力,请至\"我的账户\"中查看结果";
    public void initRunnable() {
        if (overTime == null) {
            overTime = new Runnable() {
                @Override
                public void run() {
                    if (isGamePlaying) {
                        // go.setEnabled(true);
                        // isGamePlaying = false;
                        isOverTime = true;
                        if (TextUtils.isEmpty(resultMsg)) {
                            resultMsg = newlaba_overtime;
                        }
                        setLabaRecordOutTime();
                        wheelStopDelay(WHEELRESULT_NO);
                    } else {
                        // wheelStopDelay(WHEELRESULT_NO);
                    }
                }
            };
        }
    }

    private double getTotalPointDoubleFormat(String useTotalPointsString) {
        if (TextUtils.isEmpty(useTotalPoints)) {
            return 0.00;
        } else {
            return Double.valueOf(useTotalPoints);
        }
    }
    /** 进度条 */
    private LabaProgressBarView progressBar;
    private double currentLevelMax = 1;
    private void updateProgressBar() {
        double useTotalPointDouble;
        useTotalPointDouble = getTotalPointDoubleFormat(useTotalPoints);
        double percent = (useTotalPointDouble / currentLevelMax);
        progressBar.setProgress(percent);
    }
    boolean levelSwitch = false;
    Random random = new Random();
    int is;
    private void run(){
        if (levelSwitch) {

//            findCurrentLevelMax();
            updateProgressBar();
        }
        earnPoints = "50";


//        if (response.isGameSuccess()) {
//
//            if (response.isRedeemProduct()) {
        is = random.nextInt(10);
        if(is%2 == 0){
            handleRedeemAward();
        } else if(is %3 == 0){
            handlePointsAward(100,"");
        } else {
            wheelStop(WHEELRESULT_NO);
        }

//
//            } else {

//            }
//        } else if (response.isExceedDailyLimit()) {
//            resultMsg = getString(R.string.newlaba_exceed_daily_limit);
//            wheelStop(WHEELRESULT_NO);
//        } else if (response.isUnAward()) {
//            resultMsg = getString(R.string.newlaba_failed);
//            wheelStop(WHEELRESULT_NO);
//        } else {
//            // resultMsg = getString(R.string.newlaba_outhand);
//            // wheelStop(WHEELRESULT_NO);
//            resultMsg = response.getErrorMessage();
//            wheelStop(WHEELRESULT_NO);
//        }
    }

    String productId = TEN_WIN;
    /** 本轮赢取积分 */
    private int winScoreThisTime = -1;
    private String winGameCash = "";
    private void handlePointsAward(int lotteryPoint,String cash) {
        resultMsg = "";
        if (TWO_WIN.equals(productId)) {
            winScoreThisTime = lotteryPoint;
            isWin = true;
            isShiWuWin = false;
            winGameCash = cash;
            winNumber = 2;
            winID = "02";
            wheelStop(WHEELRESULT_2);
        } else if (TEN_WIN.equals(productId)) {
            winScoreThisTime = lotteryPoint;
            isWin = true;
            isShiWuWin = false;
            winGameCash = cash;
            winNumber = 10;
            winID = "010";
            wheelStop(WHEELRESULT_10);
        } else if (HUNDER_WIN.equals(productId)) {
            winScoreThisTime = lotteryPoint;
            isWin = true;
            isShiWuWin = false;
            winGameCash = cash;
            winNumber = 100;
            winID = "0100";
            wheelStop(WHEELRESULT_100);
        } else {
            // 未中奖
            resultMsg = getString(R.string.newlaba_failed);
            wheelStop(WHEELRESULT_NO);
        }
    }

    int score = 0;
    private void changeScore(final int gameScore) {
        if (gameScore > 0) {
            winScore.postDelayed(new Runnable() {
                @Override
                public void run() {
                    score += gameScore;
                    winScore.setScore(score);
                    winScoreThisTime = -1;
                }
            }, 1000);
        }
    }
    private boolean isWin = false;
    private boolean isShiWuWin = false;
    private String winID = "";
    private String winName = "";
    private int winNumber = 1;
    private void handleRedeemAward(){
        resultMsg = "";
        isWin = true;
        isShiWuWin = true;
        winID = "";
        winName = "";
        winNumber = 1;
        wheelStop(WHEELRESULT_SHIWU);
        changeScore(50);
    }
    public void wheelStop(final int flag) {
        if (isOverTime) {
            return;
        }
        wheel.postDelayed(new Runnable() {
            @Override
            public void run() {
                wheelStopDelay(flag);
            }
        }, 200);
    }
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String ymd = formatter.format(curDate);
        return ymd;
    }
    private void process(){

        isGamePlaying = false;
        labaSound = new LabaSound(this);
        updateBets(ATOM_SCORE);

        String ymdDate = getCurrentDate();
        String storedYmdDate = MyPreference.getInstance().getLabaYMD4PopupDialog();
//        if (!ymdDate.equals(storedYmdDate)) {
//            MyPreference.getInstance().storeLabaYMD4PopupDialog(ymdDate);
//            labaRequestManager.requestCMSDailyAnnounce();
//        }
//        if (UserInfoCommon.getInstance().isLogined()) {
//            labaRequestManager.requestAddPoints();
//            labaRequestManager.requestCMSList();
//            labaRequestManager.requestCMSAds();
//        }

        gameStartAnimation();
    }
    /**
     * 初始化动画
     */
    private void gameStartAnimation() {
        isGamePlaying = true;
        changEditTextState(false);
        go.setEnabled(false);

        wheel.startInitAnimation(WheelView.INDEX_GOLD, WheelView.INDEX_GOLD, WheelView.INDEX_GOLD);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ivBack:
                    finish();
                    break;
                case R.id.ivSound:
                    setPlaySound();
                    storeAndPlaySound();
                    break;
                case R.id.plane:
                    handlePlaneClick();

                    break;
                case R.id.maxBets:
                    handleMaxBetsClick();

                    break;
                case R.id.game_rule:
                    handleGameRuleClick();
                    break;
                case R.id.history:
                    handleHistoryClick();
                    break;
                case R.id.go:

                    break;
            }
        }
    };
    private void handleHistoryClick() {

    }

    private void handleGameRuleClick()
    {
        playSounds(3, 0);
//        Intent intent = new Intent(LabaNewActivity.this,LabaGameRuleActivity.class);
//        startActivity(intent);
    }
    private void handlePlaneClick() {

    }
    private void handleMaxBetsClick() {
        playSounds(3, 0);
        showMaxDialog();
    }
    private void showMaxDialog() {
        final CustomDialog msimpleDialog = new CustomDialog(this, R.layout.layout_confirm_dialog_forlaba, R.style.dialog, true);
        msimpleDialog.setMessage("真的确定要全部投注吗？\n（最高投注10W）");
        msimpleDialog.setRightButtonText("确定");
        msimpleDialog.setLeftButtonText("取消");
        msimpleDialog.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!isFinishing() && null != msimpleDialog && msimpleDialog.isShowing()) {
                    msimpleDialog.dismiss();
                }
                int currentBets = Integer.parseInt(balance.getText().toString().replaceAll(",", ""));
                if (currentBets >= MAX_BETS) {
                    updateBets(MAX_BETS);
                } else {
                    int remainder = currentBets % ATOM_SCORE;
                    updateBets(currentBets - remainder);
                }
            }
        });
        msimpleDialog.setLeftButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!isFinishing() && null != msimpleDialog && msimpleDialog.isShowing()) {
                    msimpleDialog.dismiss();
                }
            }
        });
        msimpleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {
                if (null != msimpleDialog && msimpleDialog.isShowing()) {
                    msimpleDialog.dismiss();
                }
            }
        });
        msimpleDialog.show();
    }
    int customPoints = 5000;
    String useTotalPoints = String.valueOf(customPoints);
    /** 单次加减分数 */
    private final int ATOM_SCORE = 50;
    /** 最大投注积分 */
    private final int MAX_BETS = 100000;
    /** 最小投注积分 */
    private final int MIN_BETS = 50;

    /** 投注积分 */
    private int bets = ATOM_SCORE;
    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (Integer.parseInt(s.toString()) != bets) {
                updateBets(Integer.parseInt(s.toString()));
                // bets = Integer.parseInt(s.toString());
                // betsText.setText(String.valueOf(bets));
            }
        }
    };


    /**
     * 校验投注分数，并更新UI
     *
     * @param targetBets
     *            目标分数
     */
    private void updateBets(int targetBets){
        reduceScore.setEnabled(true);
        addScore.setEnabled(true);
        //转成50的位数
        int reminder = targetBets%ATOM_SCORE;
        targetBets = targetBets - reminder;
        if(targetBets >= MAX_BETS){
            targetBets = MAX_BETS;
            addScore.setEnabled(false);
        } else if(targetBets <= MIN_BETS){
            targetBets = MIN_BETS;
            reduceScore.setEnabled(false);
        }
        bets = targetBets;
        ivBets.setText(String.valueOf(bets));
        balance.setText(String.valueOf(customPoints));
    }


    private LabaSound labaSound;
    private void setPlaySound() {
        playSounds(3,0);
        isSoundOn = !isSoundOn;
        ivSound.setImageResource(isSoundOn?R.drawable.laba_sound_on_selector:R.drawable.laba_sound_off_selector);
    }
    private void storeAndPlaySound(){
        MyPreference.getInstance().storeLabaSound(isSoundOn);
        if (isSoundOn) {
            // if (isGamePlaying) {
            playSound();
            // }
        } else {
            stopSound();
        }
    }

    private void stopSound(){
        try{
            if(labaSound.isMediaPlaying()){
                labaSound.stopMediaPlayer();
            }
        }catch (Exception e){

        }
    }
    private void playSounds(int sound, int number) {
        if (!isSoundOn) {
            return;
        }
        try {
            labaSound.play(sound, number);
            // soundPool.play(soundPoolMap.get(sound), 1, 1, 1, number, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playSound(){
        if (!isSoundOn) {
            return;
        }
        try {
            if (labaSound.isMediaPlaying()) {
                return;
            }
            labaSound.startMediaPlayer();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playSound();
        // 奖杯角标
        subscript.setCount(MyPreference.getInstance().getLabaHistoryCount());
    }


    private final LabaResultBean record = new LabaResultBean();
    @Override
    protected void onDestroy() {
        MyPreference.getInstance().storeLabaHistoryCount(0);
        if (labaSound.getSoundPool() != null) {
            labaSound.releaseSoundPool();
            labaSound = null;
        }
        if (plane != null) {
            plane.cancel();
            plane = null;
        }
        super.onDestroy();
    }
    private void setLabaRecordOutTime(){
        record.setResTime(null);
        record.setMemberId("");
        record.setTimeoutFlag("1");
    }

    private void setRecordImage(int first, int second, int third) {
        StringBuilder imageBuilder = new StringBuilder();
        imageBuilder.append(WheelView.map.get(first));
        imageBuilder.append("," + WheelView.map.get(second));
        imageBuilder.append("," + WheelView.map.get(third));
        record.setImageCode(imageBuilder.toString());
    }

    private void saveRecord() {
//        LabaResultsDao.INSTANCE.insertRecord(record);
//        UploadLabaResultPollingUtils.startPollingService(this);
    }
    private int[] winBitmapList = { -1, -1, -1 };
    private int win2Fruit = -1;
    private void wheelStopDelay(int flag){
        switch (flag){
            case WHEELRESULT_NO:
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < 6; i++) {
                    list.add(i);
                }
                Collections.shuffle(list);
                wheel.stop(list.get(0), list.get(1), list.get(2));
                setRecordImage(list.get(0), list.get(1), list.get(2));
                saveRecord();
                list.clear();
                break;
            case WHEELRESULT_SHIWU:
                wheel.stop(WheelView.INDEX_UNKNOWN, WheelView.INDEX_UNKNOWN, WheelView.INDEX_UNKNOWN);
                winBitmapList = new int[] { WheelView.INDEX_UNKNOWN, WheelView.INDEX_UNKNOWN, WheelView.INDEX_UNKNOWN };
                setRecordImage(WheelView.INDEX_UNKNOWN, WheelView.INDEX_UNKNOWN, WheelView.INDEX_UNKNOWN);
                saveRecord();
                break;

            case WHEELRESULT_100:
                wheel.stop(WheelView.INDEX_GOLD, WheelView.INDEX_GOLD, WheelView.INDEX_GOLD);
                winBitmapList = new int[] { WheelView.INDEX_GOLD, WheelView.INDEX_GOLD, WheelView.INDEX_GOLD };
                setRecordImage(WheelView.INDEX_GOLD, WheelView.INDEX_GOLD, WheelView.INDEX_GOLD);
                saveRecord();
                break;
            case WHEELRESULT_10:
                List<Integer> list2 = new ArrayList<Integer>();
                list2.add(WheelView.INDEX_LEMON);
                list2.add(WheelView.INDEX_MANGO);
                list2.add(WheelView.INDEX_STRAWBERRY);
                list2.add(WheelView.INDEX_WATERMELON);
                Collections.shuffle(list2);
                wheel.stop(list2.get(0), list2.get(0), list2.get(0));
                winBitmapList = new int[] { list2.get(0), list2.get(0), list2.get(0) };
                setRecordImage(list2.get(0), list2.get(0), list2.get(0));
                saveRecord();
                list2.clear();
                break;
            case WHEELRESULT_2:
                List<Integer> list3 = new ArrayList<Integer>();
                list3.add(WheelView.INDEX_LEMON);
                list3.add(WheelView.INDEX_MANGO);
                list3.add(WheelView.INDEX_STRAWBERRY);
                list3.add(WheelView.INDEX_WATERMELON);
                Collections.shuffle(list3);
                int position = (int) (Math.random() * 3);
                if (position == 0) {
                    wheel.stop(list3.get(1), list3.get(0), list3.get(0));
                    winBitmapList = new int[] { list3.get(1), list3.get(0), list3.get(0) };
                    win2Fruit = list3.get(0);
                    setRecordImage(list3.get(1), list3.get(0), list3.get(0));
                    saveRecord();
                } else if (position == 1) {
                    wheel.stop(list3.get(0), list3.get(1), list3.get(0));
                    winBitmapList = new int[] { list3.get(0), list3.get(1), list3.get(0) };
                    win2Fruit = list3.get(0);
                    setRecordImage(list3.get(0), list3.get(1), list3.get(0));
                    saveRecord();
                } else {
                    wheel.stop(list3.get(0), list3.get(0), list3.get(1));
                    winBitmapList = new int[] { list3.get(0), list3.get(0), list3.get(1) };
                    win2Fruit = list3.get(0);
                    setRecordImage(list3.get(0), list3.get(0), list3.get(1));
                    saveRecord();
                }
                list3.clear();
                break;
            default:
                break;
        }
    }


    /**
     * 校验投注分数规则 1.数字 2.大于最小积分 3.小于最大积分 4.50 的倍数
     *
     * @param targetBets
     *            目标分数
     * @param myBets
     * @return result 0:成功 1:输入不合法 2:积分过小 3:积分过大 4:积分不是50的倍数 5:积分余额不足
     */
    public int checkBets(String targetBets, String myBets) {
        int result = 0;
        try {
            int gameBets = 0;
            double allBets = 0;

            gameBets = Integer.parseInt(targetBets);
            String temp = myBets.replaceAll(",", "");
            allBets = Double.parseDouble(temp);
            if (gameBets < MIN_BETS) {
                result = 2;
            }
            if (gameBets > MAX_BETS) {
                result = 3;
            }
            int remainder = gameBets % ATOM_SCORE;
            if (remainder != 0) {
                result = 4;
            }
            if (gameBets > allBets) {
                result = 5;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 1;
        }
        return result;
    }


    private void changEditTextState(final boolean flag) {
        ivBets.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivBets.setEnabled(flag);
            }
        }, 100);
    }
}
