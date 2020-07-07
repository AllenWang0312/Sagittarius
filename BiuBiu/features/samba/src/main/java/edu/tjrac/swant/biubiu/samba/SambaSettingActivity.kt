package edu.tjrac.swant.biubiu.samba

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import edu.tjrac.swant.Router
import edu.tjrac.swant.baselib.util.T
import jcifs.smb.NtlmPasswordAuthentication
import jcifs.smb.SmbException
import jcifs.smb.SmbFile
import kotlinx.android.synthetic.main.activity_samba_setting.*
import java.io.IOException
import java.net.MalformedURLException
import java.net.UnknownHostException

@Route(path = Router.samba_main)
class SambaSettingActivity : AppCompatActivity() {

    //    var paths: LinkedList<SmbFile>? = null
    var items: ArrayList<SmbFile>? = ArrayList()

    var adapter: SambaFileAdapter? = null

    var hosts:Array<SmbFile>?=null

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (hosts?.size!! > 0) {
                items?.addAll(hosts!!)
                adapter?.notifyDataSetChanged()
            }
            T.Companion.show(this@SambaSettingActivity,"扫描完毕,发现${hosts?.size}个共享磁盘")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_samba_setting)
        adapter = SambaFileAdapter(items)
        recycler?.layoutManager = LinearLayoutManager(this)
        recycler?.adapter = adapter



        bt_test?.setOnClickListener {
            var ip = et_ip?.text?.toString()
            var share = et_dir?.text?.toString()
            var user = et_user?.text?.toString()
            var pass = et_pass?.text?.toString()
            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {

            } else {
                try {
                    jcifs.Config.registerSmbURLHandler()
                    //                    var url = "smb://$user:$pass@$ip/$share"
                    var url = "smb://$ip/$share"
                    var auth = NtlmPasswordAuthentication(null, user, pass);
                    var sfile = SmbFile(url, auth)
                    sfile.listFiles()
                } catch (e: SmbException) {

                } catch (e: MalformedURLException) {

                } catch (e: UnknownHostException) {

                } catch (e: IOException) {

                }
            }

        }

       Thread(Runnable {
           try {
               hosts = SmbFile("smb://").listFiles()
               handler?.sendEmptyMessage(1)
           } catch (e: Exception) {
               e.printStackTrace()
           }
           //            catch (e: ExceptionInInitializerError) {
           //                e.printStackTrace()
           //            }
       }).start()
    }
}