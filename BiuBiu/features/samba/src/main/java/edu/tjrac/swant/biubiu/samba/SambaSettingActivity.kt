package edu.tjrac.swant.biubiu.samba

import android.os.Bundle
import android.text.TextUtils
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import jcifs.smb.NtlmPasswordAuthentication
import jcifs.smb.SmbException
import jcifs.smb.SmbFile
import kotlinx.android.synthetic.main.activity_samba_setting.*
import java.io.IOException
import java.net.MalformedURLException
import java.net.UnknownHostException

class SambaSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_samba_setting)

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
    }
}