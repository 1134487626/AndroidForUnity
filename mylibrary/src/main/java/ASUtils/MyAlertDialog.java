package ASUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import UnityBasic.Delegate;
import UnityBasic.Extends;

/**
 * Created by Administrator on 2018/7/20.
 */

public class MyAlertDialog {

    private Delegate result;
    private JSONObject varJoan;

    private Activity getUnity() {
        return UnityPlayer.currentActivity;
    }

    /**
     * 检查是否有需要传递给Unity的参数
     */
    private void isDelegate() {

        try {
            String gameName = varJoan.getString("gameName");
            String funcName = varJoan.getString("funcName");
            result = new Delegate(gameName, funcName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showSimpleAlertDialog(String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getUnity());
        dialog.setTitle("提示：");//设置标提
        dialog.setMessage(message);//设置提示内容
        dialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        dialog.show();//显示这个弹窗界面
    }

    public void showAlertDialog(String json) {

        try {
            varJoan = new JSONObject(json);
            isDelegate();
            AlertDialog.Builder dialog = new AlertDialog.Builder(getUnity());
            String title = varJoan.getString("title");
            String message = varJoan.getString("message");
            final OpenResultType resultType = OpenResultType.values()[varJoan.getInt("type")];
            dialog.setTitle(!Extends.isNullOrEmpty(title) ? title : "温馨提示：");//设置标提
            dialog.setMessage(!Extends.isNullOrEmpty(message) ? message : resultType.toString());//设置提示内容

            final boolean isCancel = varJoan.getBoolean("isCancel");
            if (isCancel) {
                dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();//设置左边按钮监听,点击取消就关闭这个弹窗
                    }
                });
            }

            //设置右边按钮监听,点击确定创建弹窗回话意图,否则关闭这个弹窗
            dialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    if (isCancel)
                        alertDialogIntent(resultType);
                    else
                        arg0.dismiss();
                }
            });
            dialog.show();//显示这个弹窗界面

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行视图弹出操作
     */
    private void alertDialogIntent(OpenResultType resultType) {

        Intent intent;
        switch (resultType) {
            case GPS:
                intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);//跳转到系统定位服务界面
                getUnity().startActivityForResult(intent, 0);                 //设置完成后返回到原来的界面
                break;
            case PHOTOS:
                intent = new Intent(Intent.ACTION_PICK);//选择操作
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//打开相册
                getUnity().startActivityForResult(intent, resultType.ordinal());//返回结果
                break;
            case CAMERA:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开摄像机
                File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);//拍照完成后将图片保存到这个路径下
                getUnity().startActivityForResult(intent, resultType.ordinal());//返回结果
                break;
            case BROWSER:
                intent = new Intent(Intent.ACTION_VIEW);//打开一个活动视图
                try {
                    intent.setData(Uri.parse(varJoan.getString("extend")));                 //传入网址数据访问
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getUnity().startActivity(intent);
                break;
            case NoWXApp:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        OpenResultType type = OpenResultType.values()[requestCode];
        switch (type) {
            case PHOTOS:
                String photosPath = getImagePath(data.getData());
                result.invoke2(photosPath);
                break;
            case CAMERA:
                result.invoke2(startPhotoZoom("temp"));//保存到摄像路径下并返回给Unity
                break;
            case BROWSER:
                break;
        }
    }

    /**
     * 设置保存摄像的图片
     *
     * @param saveName
     */
    public String startPhotoZoom(String saveName) {

        String fullName = Environment.getExternalStorageDirectory() + "/" + saveName + ".jpg";
        Uri uri = Uri.fromFile(new File(fullName));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        return fullName;
    }

    /**
     * @param uri
     * @return 获取选中手机图片的路径
     */
    public String getImagePath(Uri uri) {
        String path = null;
        if (uri != null) {
            final String scheme = uri.getScheme();
            if (null == scheme) {
                path = uri.getPath();
            } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                path = uri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                String[] temp = {MediaStore.Images.Media.DATA};
                Cursor cursor = getUnity().getContentResolver().query(uri, temp, null, null, null);
                int nPhotoColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(nPhotoColumn);
                cursor.close();
            }
        }
        return path;
    }

    public void SavePathBitmap(String path) {

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            SaveBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveBitmap(Bitmap bitmap) throws IOException {

        FileOutputStream fOut = null;
        String path = "/mnt/sdcard/DCIM/";
        try {
            //查看这个路径是否存在，如果并没有这个路径，创建这个路径
            File destDir = new File(path);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            fOut = new FileOutputStream(path + "/" + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //将Bitmap对象写入本地路径中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
