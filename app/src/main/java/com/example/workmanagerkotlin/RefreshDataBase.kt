package com.example.workmanagerkotlin

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters

/*

414 - Worker Sınıf Kullanımı

Burada aşağıda RefreshDataBase ısımlı bır class olusturduk ve bunun bır worker sınıfı oldugunu belirledik.

SharedPreferences ile Integerlarımızı kaydedecegız databaseyi.

--> RefreshDataBase : Worker  -> Burada worker sınıfından kalıtım aldık.Hata alınca alt + enter ile implement members dedik.doWork fonksiyonu gelecek.

Ardında dokümanasyona bakarak aşağıdakı kodu yazdık. Hata gelen yerlere alt + enter basarak hataları gıderdık.

Burada ornek uzerınden konuyu anlayacagız kullanıcının sadece yasını alarak 1 ekleyecegız ve sharedPreferneces ıle bunu kaydedecegiz.

aşağıda sharedpreferencesı kullanmak ıcın contextı bır degısken halıne getırmemız gerekıyor.


 */

class RefreshDataBase(val context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    override fun doWork(): Result { //Bu override ettıgımız fonksıyon bızden bır tane result ısımlı bır sey dondurmemızı ıstıyor.Return kullanmamız lazım.
        //burada ne yapmak ıstıyorsak uygulamada doWork fonksıyonu altında yapabiliyoruz.
        val getData = inputData
        val myNumber = getData.getInt("intKey",0)   //burada getInt ile gonderdıgımız Integer'ı alıyoruz.
        refreshDataBase(myNumber)
        return Result.success()
    }


    private fun refreshDataBase(myNumber : Int) {

        val sharedPreferences = context.getSharedPreferences("com.example.workmanagerkotlin",Context.MODE_PRIVATE)
        var mySavedNumber = sharedPreferences.getInt("myNumber",0)  //ney kaydettıgımızı .get ile ulaşabiliyoruz ardından key value eşeleşmesi istiyor.
        //ismini myNumber dedik ve bizden son olarak bır sey kaydedılmezse (boş gelirse)  default deger ne alayım dıye soracak onada 0 diyeceğiz.

        mySavedNumber = mySavedNumber + myNumber
        println(mySavedNumber)

        sharedPreferences.edit().putInt("myNumber",mySavedNumber).apply()  // .edit ile değeri değiştireceğim.Birebir aynı anahtar kelimeyi yazıyorum. koymak ıstedıgımız degerıde
        //mysavedNumber ile yazdık.

    }

}