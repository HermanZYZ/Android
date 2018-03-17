package com.example.zyz.mapandlocation;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlayOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.R.attr.name;
import static com.amap.api.col.sln3.dk.r;
import static com.example.zyz.mapandlocation.R.drawable.hot;

//监听定位和定位变化
public class MainActivity extends AppCompatActivity implements LocationSource, AMapLocationListener ,AMap.OnMapClickListener{

    //显示地图需要的变量
    private MapView mapView;//地图控件
    private AMap aMap;//地图对象

    //Button
    private Button HotMap;
    private Boolean IF_Hot_Map = false;


    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HotMap = (Button) findViewById(R.id.hotmap);

        //显示地图
        mapView = (MapView) findViewById(R.id.map);
        //必须要写
        mapView.onCreate(savedInstanceState);
        //获取地图对象
        aMap = mapView.getMap();
        aMap.showIndoorMap(true);

        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        //地图是否可点击
        aMap.setOnMapClickListener(this);


        //定位的小图标 默认是蓝点 这里自定义一团火，其实就是一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.locate));
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);

        //生成热力点坐标列表
        LatLng[] latlngs = new LatLng[750];
        double x = 23.071460204 - 23.0649356650;
        double y = 113.3166335014 - 113.3090450001;

        double x_ = 0;
        double y_ = 0;

        int i = 0;
        //校区
        for (;i < 400;i++) {
            x_ = (Math.random() - 0.5) * x + 23.0649356650;
            y_ = (Math.random() - 0.5) * y + 113.3890450001;
            latlngs[i] = new LatLng(x_, y_);
        }
        //图书馆
        for(;i<500;i++)
        {
            x_ = (Math.random() - 0.5) * 0.001 + 23.0667618224;
            y_ = (Math.random() - 0.5) * 0.001 + 113.3918130398;
            latlngs[i] = new LatLng(x_, y_);
        }
        //教学楼
        for(;i<600;i++)
        {
            x_ = (Math.random() - 0.5) * 0.0015 + 23.0683066335;
            y_ = (Math.random() - 0.5) * 0.001 + 113.3930798573;
            latlngs[i] = new LatLng(x_, y_);
        }
        //院楼
        for(;i<750;i++)
        {
            x_ = (Math.random() - 0.5) * 0.0023 + 23.070994;
            y_ = (Math.random() - 0.5) * 0.0031 + 113.394542;
            latlngs[i] = new LatLng(x_, y_);
        }

        // 构建热力图 HeatmapTileProvider
        HeatmapTileProvider.Builder builder = new HeatmapTileProvider.Builder();
        builder.data(Arrays.asList(latlngs));// 设置热力图绘制的数据
            //.gradient(ALT_HEATMAP_GRADIENT); // 设置热力图渐变，有默认值 DEFAULT_GRADIENT，可不设置该接口
        // Gradient 的设置可见参考手册
        // 构造热力图对象
        HeatmapTileProvider heatmapTileProvider = builder.build();

        // 初始化 TileOverlayOptions
        final TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
        tileOverlayOptions.tileProvider(heatmapTileProvider); // 设置瓦片图层的提供者


        //开始定位
        initLoc();

//        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//
//                int num = (int) (Math.random() * 500 + 500);
//                Toast.makeText(getApplicationContext(),"附近大概" + String.valueOf(num) + "人", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        HotMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (IF_Hot_Map) /*当前处在热力图层*/ {
                    aMap.clear();
                    IF_Hot_Map = false;
                } else/*当前处在Home界面*/ {
                    // 向地图上添加 TileOverlayOptions 类对象
                    aMap.clear();
                    aMap.addTileOverlay(tileOverlayOptions);
                    IF_Hot_Map = true;
                }
            }
        });

        MarkListener();
    }

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    //添加图钉
//                    aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;

    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

//    public static void verifyStoragePermissions(Activity activity) {
//        try {
//            //检测是否有读的权限
//            int permission = ActivityCompat.checkSelfPermission(activity,
//                    "android.permission.READ_EXTERNAL_STORAGE");
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                // 没有读的权限，去申请读的权限，会弹出对话框
//                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            onDestroy();
        else
        {
            Toast.makeText(MainActivity.this, "已授权",Toast.LENGTH_SHORT).show();
        }
    }

//    private void readCsv(String name) {
//        int i = 0;// 用于标记打印的条数
//        try {
//            File csv = new File(Environment.getExternalStorageDirectory() + "/" + name + ".csv"); // CSV文件路径
//            BufferedReader br = new BufferedReader(new FileReader(csv));
//            br.readLine();
//            String line = "";
////            Log.e("before read", csv.getAbsolutePath());
//            while ((line = br.readLine()) != null) { // 这里读取csv文件中的前10条数据
//                i++;
////                System.out.println("第" + i + "行：" + line);// 输出每一行数据
//                /**
//                 *  csv格式每一列内容以逗号分隔,因此要取出想要的内容,以逗号为分割符分割字符串即可,
//                 *  把分割结果存到到数组中,根据数组来取得相应值
//                 */
//                String buffer[] = line.split(",");// 以逗号分隔
////                System.out.println("第" + i + "行：" + buffer[0]);// 取第一列数据
////                System.out.println("第" + i + "行：" + buffer[1]);
//                dbHelper.insert(Double.valueOf(buffer[0]), Double.valueOf(buffer[1]));
//            }
//            Log.e("Number", String.valueOf(i));
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("file error", e.getMessage());
//        }
//    }

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
//        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.note));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("一层约178人，二层约267人，三层356人");
        //设置是否可拖曳
        options.draggable(true);
        //设置多少帧刷新一次图片资源
        options.period(60);
        return options;

    }

    private static final double[] Latitude = {23.0663, 23.0689, 23.0676, 23.0662, 23.0657, 23.0686};
    private static final double[] Longitude = {113.391,113.392, 113.392, 113.387, 113.386, 113.392};
    private static final String[] Name = {"图书馆", "教学楼A", "教学楼E", "北基础实验楼", "环境科学实验中心", "教学楼B"};

    //地图点击事件
    @Override
    public void onMapClick(LatLng latLng) {
//点击地图后清理图层插上图标，在将其移动到中心位置
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        Log.e(String.valueOf(latitude),String.valueOf(longitude));

        for(int i = 0;i < 6; i++)
        {
            if(Math.abs(latitude - Latitude[i]) < 0.0001 && Math.abs(longitude - Longitude[i]) < 0.001)
            {
                aMap.clear();
                MarkerOptions otMarkerOptions = new MarkerOptions();
//        otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.note));
                otMarkerOptions.position(latLng);
                otMarkerOptions.title(Name[i]);
                otMarkerOptions.snippet("1层: " + String.valueOf((int)(Math.random()*100)) +
                        "\n2层: " + String.valueOf((int)(Math.random()*100)) +
                        "\n3层: " + String.valueOf((int)(Math.random()*100)) +
                        "\n4层: " + String.valueOf((int)(Math.random()*100)) +
                        "\n5层: " + String.valueOf((int)(Math.random()*100))
                );
                aMap.addMarker(otMarkerOptions);
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                break;
            }
        }
    }

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }
    };

    private void MarkListener()
    {
        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.isFlat())
                    marker.destroy();
                else
                {
                    marker.showInfoWindow();
                    marker.setFlat(true);
                }
                return true;
            }
        };
// 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);
    }
}



