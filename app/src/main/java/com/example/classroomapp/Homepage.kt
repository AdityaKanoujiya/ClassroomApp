package com.example.classroomapp


//import TodoFragment
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.search.SearchBar

import com.example.classroomapp.databinding.ActivityHomepageBinding

class Homepage : AppCompatActivity() //, OnClassCreatedListener
{

    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_layout, HomeFragment(), "HomeFragment")
                .commit()
        }

        var home : ImageButton = binding.home
        var todo : ImageButton = binding.todo
        val chat : ImageButton = binding.chats
        var profileImg : ImageView = binding.profileImg
        var searchBar : SearchBar = binding.searchBar
        var profile = binding.profile

        val sharedPreferences: SharedPreferences = getSharedPreferences("PrefsFile", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val personPhoto = acct.photoUrl

            Glide.with(this)
                .load(personPhoto)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImg)

            searchBar.hint = personName.toString()

            Log.w("cs",personName.toString())
            Log.w("cs",personPhoto.toString())
            editor.putString("userName", personName)
            editor.putString("profileImg",personPhoto.toString())
            editor.apply()
        }

        profileImg.setOnClickListener(){
//            sideSheet.visibility= View.VISIBLE
        }

        home.setOnClickListener(){
            home.setImageResource(R.drawable.baseline_home_24)
            replaceFragment(HomeFragment())
//            searchBar.visibility=View.VISIBLE
            searchBar.visibility= View.GONE
        }

        todo.setOnClickListener(){
            home.setImageResource(R.drawable.home)
            replaceFragment(TodoFragment())
            searchBar.visibility= View.GONE
        }

        chat.setOnClickListener(){
            replaceFragment(ChatFragment())
//            searchBar.visibility=View.VISIBLE
            searchBar.visibility= View.GONE
        }

        profile.setOnClickListener(){
            replaceFragment(PomoFragment())
            searchBar.visibility= View.GONE
        }
    }

//    override fun onClassCreated(batch: String, subject: String, teacherName: String) {
//        val homeFragment = supportFragmentManager.findFragmentByTag("HomeFragment") as HomeFragment
//        homeFragment.addClass(ClassDataClass(batch, subject, teacherName))
//    }

    fun replaceFragment(fragment: Fragment){
        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.Fragment_layout,fragment)
        transaction.commit()
    }


}
