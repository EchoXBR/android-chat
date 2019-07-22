package cn.wildfire.chat.kit.net;



import cn.wildfire.chat.app.login.model.LoginResult;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼                  BUG辟易
 *
 * @author :EchoXBR in  2019/7/5 下午4:25.
 * 功能描述:网络请求方法封装
 */
public class HttpMethods {

    private static final Object LOCK = new Object();
    private static HttpMethods httpMethods;
    private String BaseURL = "http://" + Config.APP_SERVER_HOST + ":" + Config.APP_SERVER_PORT;

    public static HttpMethods getInstance() {
        if (httpMethods == null) {
            synchronized (LOCK) {
                if (httpMethods == null) {
                    httpMethods = new HttpMethods();
                }
            }
        }
        return httpMethods;
    }




    public void login(String sendData, Observer<LoginResult> observer) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), sendData);
        RetrofitCreateHelper.createApi(Api.class, BaseURL)
                .login(requestBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
