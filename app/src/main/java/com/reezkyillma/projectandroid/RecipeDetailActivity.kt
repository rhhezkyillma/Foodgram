package com.reezkyillma.projectandroid
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.reezkyillma.projectandroid.Model.Recipes
import com.reezkyillma.projectandroid.API.ApiClient
import com.reezkyillma.projectandroid.API.BaseUrl
import com.reezkyillma.projectandroid.API.OnlyApi
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_recipe.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var article : Recipes
    private lateinit var imagePoster : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_recipe)
        imagePoster =  findViewById(R.id.image)
        val onlyApi : OnlyApi = ApiClient.getClient().create(OnlyApi::class.java)
        val i = intent
        val id = i.getSerializableExtra("id")
        getDetailArticle(onlyApi, id.toString())
        back.setOnClickListener {
            onBackPressed()

        }

    }

    private fun getDetailArticle(onlyApi: OnlyApi, id: String) {
        val call : Call<Recipes> = onlyApi.getArticlebyid(id)
        call.enqueue(object : Callback<Recipes>{
            override fun onFailure(call: Call<Recipes>?, t: Throwable?) {
                Log.d("TAG", "Gagal Fetch Detail Article")
            }

            override fun onResponse(call: Call<Recipes>?, response: Response<Recipes>?) {
                article = response?.body()!!
                Log.d("TAG", "Movie size ${article}")
//                collapseToolbar.title = article.getTitle()
                textView_title.text =  article.getTitle()
//                txtIngredient.text =  article.getIngredient()
//                txtDirection.text =  article.getDirection()
//                Description.text =  article.getDescription()
                val desc = article.getDescription().toString()
                var newDesc = desc.replace("<p>","")
                newDesc =newDesc.replace("</p>","")
                newDesc =newDesc.replace("<strong>","")
                newDesc =newDesc.replace("</strong>","")
                newDesc =newDesc.replace("<em>", "")
                newDesc =newDesc.replace("</em>", "")
                newDesc =newDesc.replace("&nbsp", "")
                newDesc =newDesc.replace("<li>", "")
                newDesc =newDesc.replace("</li>", "")
                newDesc =newDesc.replace(";", "")
                newDesc =newDesc.replace("&quot;tajam&quot;", "")
                newDesc =newDesc.replace(";;", "")
                newDesc =newDesc.replace("</a>", "")
                newDesc =newDesc.replace("<a href=\"https://www.kreasisasa.com/cookingnary/tips-masak/tips-memasak-seafood-ala-chef-profesional\" target=\"blank\">", "")
                newDesc =newDesc.replace("<br>", "")
                newDesc =newDesc.replace("<a href=\"https://www.kreasisasa.com/cookingnary/tips-masak/tips-memasak-seafood-ala-chef-profesional\" target=\"blank\"></a>", "")
                newDesc =newDesc.replace("<p data-f-id=\"pbf\" ", "")
                newDesc =newDesc.replace("style=\"text-align: center; font-size: 14px; margin-top: 30px; opacity: 0.65; font-family: sans-serif;\">Powered by ", "")
                newDesc =newDesc.replace("<p data-f-id=\"pbf\" style=\"text-align: center; font-size: 14px; margin-top: 30px; opacity: 0.65; font-family: sans-serif;\">Powered by <a href=\"https://www.froala.com/wysiwyg-editor?pb=1\" title=\"Froala Editor\">Froala Editor</a>","")
                Description.text = newDesc

                val ingredient = article.getIngredient().toString()
                var newingredient = ingredient.replace("<p>","")
                newingredient =newingredient.replace("</p>","")
                newingredient =newingredient.replace("<strong>","")
                newingredient =newingredient.replace("</strong>","")
                newingredient =newingredient.replace("<em>", "")
                newingredient =newingredient.replace("</em>", "")
                newingredient =newingredient.replace("&nbsp", "")
                newingredient =newingredient.replace("<li>", "")
                newingredient =newingredient.replace("</li>", "")
                newingredient =newingredient.replace("<;>", "")
                newingredient =newingredient.replace("<br>", "")
                newingredient =newingredient.replace("null", "")
                newingredient =newingredient.replace("<ul>", "")
                newingredient =newingredient.replace("</ul>", "")
                newingredient =newingredient.replace("<ol>", "")
                newingredient =newingredient.replace("</ol>", "")
                newingredient =newingredient.replace("<div>", "")
                newingredient =newingredient.replace("</div>", "")
                newingredient =newingredient.replace("; ; ; ; ;;", "")
                newingredient =newingredient.replace("&frac12;", "")
                newingredient =newingredient.replace("&frac14;", "")
                newingredient =newingredient.replace("</span>", "")
                newingredient =newingredient.replace("<span style=\"line-height:115%;font-size:12.0pt;\">", "")
                txtIngredient.text = newingredient

                val direction = article.getDirection().toString()
                var newdirection = direction.replace("<p>","")
                newdirection =newdirection.replace("</p>","")
                newdirection =newdirection.replace("<strong>","")
                newdirection =newdirection.replace("</strong>","")
                newdirection =newdirection.replace("<em>", "")
                newdirection =newdirection.replace("</em>", "")
                newdirection =newdirection.replace("&nbsp", "")
                newdirection =newdirection.replace("<li>", "")
                newdirection =newdirection.replace("</li>", "")
                newdirection =newdirection.replace(";", "")
                newdirection =newdirection.replace("<br>", "")
                newdirection =newdirection.replace("null", "")
                newdirection =newdirection.replace("<ul>", "")
                newdirection =newdirection.replace("</ul>", "")
                newdirection =newdirection.replace("<ol>", "")
                newdirection =newdirection.replace("</ol>", "")
                newdirection =newdirection.replace("<div>", "")
                newdirection =newdirection.replace("</div>", "")
                newdirection =newdirection.replace("<div data-field-group=\"\"><div data-field=\"textarea\" data-field-hint=\"Tekan ENTER untuk menambah langkah\" data-field-name=\"description\" data-maxlength=\"500\" data-placeholder=\"Bagaimana membuatnya?\" itemprop=\"recipeInstructions\">","")
                newdirection =newdirection.replace(";;;", "")
                newdirection =newdirection.replace(";;", "")
                newdirection =newdirection.replace("<div data-field-group=\"\">", "")
                txtDirection.text = newdirection

//                textView_published.text =  article.getPublishedAt()
                Picasso.get().load(BaseUrl.baseUrl+article.getImage()).into(imagePoster)
//                text_content.text = article.getTextContent()


            }

        })
    }
}

