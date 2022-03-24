package com.example.workmanagerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    /*
        413 - WorkManager nedir ?
      Kullanıcı uygulamayı kapatsa bile çalışmaya devam eder hem async calısıyor hem de ıstedıgımız zaman calısıyor.
      Uygulama kapansa bile arka planda calısıyor.

      Örn:Backend servıslerıne devamlı bır log yollayabılıyoruz veya periyodik olarak int'den veri çekerken
      kendi verilerinizle değiştireceksiniz bu arka planda belirli periyodlarla yapılmasını söyleyebiliyoruz.

      workmanager ile gelen özelliklerden bir taneside workconstraints

      1-)WorkConstraints
      Örn:Mesela bu servısı wı-fı bağlı iken calıstır veya sadece sarj olurken calıstır gıbı sınırlamaları belirliyebiliyoruz

      2-)Robust scheduling
        Bunu sadece bir defaya mahsus arka planda çalıtır , 3 saat'de bir calıstır 5 saat'de bir calıstır.



    */

        val data = Data.Builder().putInt("intKey",1).build()  //Burada göndermek istediğimiz veriyi yazdık.Integer gondermek ıstedıgımız ıcın putInt ile gonderdık.
        // Aynı ıntent veya sharedPreferences gibi put ve get kullanıyoruz.Sımdı gonderdıgımız verıyı refreshDatabase'da alacagız.

        //Constraint

        val constraints = Constraints.Builder()   // ---> Constraints seçerken androidx.work seçmemiz gerekıyor cunku baska contraıntlerde cıkıyor.

            // .setRequiredNetworkType(NetworkType.CONNECTED)  // burada ağa bağlı olmasını istedik.Büyük N yazdıktan sonra ıstedıgımızı secebılıyoruz.
            .setRequiresCharging(false)
            .build()                                        //En son işlemler bittikten sonra .build ile kuruyoruz.

             /*
                                            //Builder içine  bizden bir tip isteniyor bu da bizim yazdıgımız sınıf.
        val myWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<RefreshDataBase>()  //oneTimeWorkRequest --> Bir defaya mahsus çalıştırılıyor sadece.
            .setConstraints(constraints)  //oneTimeWorkRequest'i defferable işlemler ypamak ıcın kullanabılıyoruz.Örn -> Uygulama acıldıktan 15 dk sonra sunu yap dıyebılıyoruz.
            .setInputData(data)
            //.setInitialDelay(5,TimeUnit.HOURS)
            //.addTag("myTag")
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

              */

        val myWorkRequest : PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDataBase>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

        //En az 15 dk yapabılıyoruz periodik işlemleri.Bu ıslemlerı 15 sanıyede bır yapamıyoruz.

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this,  //Burada work calısıyor mu calısmıyor mu veya hangı durumda oldugunu bu sekıldee ogrenıyoruz
            Observer {
                                                        //getWorkInfo dedikten sonra tag'e göre veya ID'ye göre sorgulayabılıyoruz.Bunu cagırdıktan sonra bızden bır ıd ısteyecek
                if (it.state == WorkInfo.State.RUNNING) {   //Burada "myWorkRequest.id" diyerek alıyoruz.liveData ile observe edebiliyoruz.Ardından if'ler ile sorgulama yaptık.
                    println("running")

                }else if (it.state == WorkInfo.State.FAILED) {
                    println("failed")

                }else if (it.state == WorkInfo.State.SUCCEEDED){
                    println("succeeded")
                }

            })

        // WorkManager.getInstance(this).cancelAllWork()   -->İptal işlemi için bu kodu kullanıyoruz.Tage göre, id'ye göre veya bütün işleri iptal edebiliyoruz.

        //Chaining  -->Bağlama işlemi anlamına geliyor.Bu da workamager bu iş bitsin şunu yap bu iş bittiken sonra şunu yap diyerek işleri sıralıyor ve bağlıyoruz.
        // Burada dikkat edilmesi gerkeen şey periodik işlemlerde bu yapılamıyor sadece oneTimeRequest'lerde bu işlem yapılıyor.

        val oneTimeRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDataBase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()


        WorkManager.getInstance(this).beginWith(oneTimeRequest)  //Burada yaptıgımız sey ılk once bunu yap, bittikten sonra
            .then(oneTimeRequest)  // bunu yap bittikten sonra
            .then(oneTimeRequest)  //bunu yap dedik. Buna chaining deniyor.
            .enqueue()

    }




}