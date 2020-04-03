package com.letsdevelopit.lfydnewapp.faqlfyd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.letsdevelopit.lfydnewapp.DashContent;
import com.letsdevelopit.lfydnewapp.ProfileSection.ProfileActivity;
import com.letsdevelopit.lfydnewapp.R;


import java.util.ArrayList;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(FaqActivity.this));
        ArrayList<Question> questions=new ArrayList<>();


        ArrayList<Answer> answers=new ArrayList<>();
        questions.add(new Question("Lfyd or let’s fund your dreams is a business development and internet services company that has been set up to help the business owners in getting more customers at the same time help the common people to shop from their  local store’s more conveniently by giving the all the information about hyper-local discounts and sales that are happening in their city along with providing them instant cashback."));
        Answer one=new Answer("What is Lfyd?",questions);
        answers.add(one);

        ArrayList<Question> questions2=new ArrayList<>();
        questions2.add(new Question(" After downloading the app You have to fill your shop details along with giving two-three pictures of shop that will be displayed on the app You don’t have to worry if you will be facing any problem during registration just give us a call and we will do the rest for you"));
        Answer two=new Answer("How to Register as Lfyd Channel Partner?",questions2);
        answers.add(two);

        ArrayList<Question> questions3=new ArrayList<>();
        questions3.add(new Question("As soon as you fill your completer details," +
                " your shop will be displayed provincially on the app the next  step is to recharge your " +
                "commission wallet You can recharge your commission wallet using lots of 3rd party apps such as" +
                " Paytm, phone pay or any UPI or debit card."));
        Answer three=new Answer("How soon my shop will start getting customers ?",questions3);
        answers.add(three);

        ArrayList<Question> questions4=new ArrayList<>();
        questions4.add(new Question("You don’t have to pay any money in advance, " +
                "You just have to recharge your wallet so that your shop name can be displayed on the list and " +
                "only when any customer comes and shops through our app then only the commission is debited from your wallet."));
        Answer four=new Answer("Why do I have to pay money in advance ?",questions4);
        answers.add(four);

        ArrayList<Question> questions5=new ArrayList<>();
        questions5.add(new Question("Though we are pretty much sure that’s not gonna happen when u join us, " +
                "still if suppose no one comes then your money will be safe in your wallet and will only " +
                "be used when customers come to your shop using our app your money will be debited."));
        Answer five=new Answer("What happens if no customer comes to my shop through your app ?",questions5);
        answers.add(five);

        ArrayList<Question> questions6=new ArrayList<>();
        questions6.add(new Question("Through lfyd, any business partners can give discounts on any" +
                " occasion they want and any type of discount they want which will be clearly visible below their" +
                " shop name and on the dashboard Just fill the details in the business offers section on the top left drawer and " +
                "select your duration of promotional discounts and we will handle the rest ."));
        Answer six=new Answer("Can I give special discounts in festive seasons ?",questions6);
        answers.add(six);

        ArrayList<Question> questions7=new ArrayList<>();
        questions7.add(new Question("We will be very happy to get any type of suggestion from anyone especially our customers" +
                " and channel partners so that we can grow our app and our channel partners sales can increase just go to the feedback" +
                " section and write any feedback you want to give."));
        Answer seven=new Answer("Can I give any suggestions to develop your app ?",questions7);
        answers.add(seven);

        ArrayList<Question> questions8=new ArrayList<>();
        questions8.add(new Question("You can directly open the support option or chat with us option in the drawer" +
                " and can chat with our customer care executive or even  directly call us on the no provided."));
        Answer eight=new Answer("If I am having any problem in the app how to contact you ?",questions8);
        answers.add(eight);

        ArrayList<Question> questions9=new ArrayList<>();
        questions9.add(new Question("We have created this application to help the businessmen" +
                " get more customers and at the same time provide common citizens a good place to check " +
                "the discounts and offers that are available in their city with ease so we really want you" +
                " all to share the app with other people so that they can use the app to get exclusive cashback" +
                " and discounts available to them and for each new merchant’s people add they get 200Rs in their " +
                "account and to each people you add you get 50 Rs in your lfyd’s wallet."));
        Answer nine=new Answer("How to use the refer and earn feature  ?",questions9);
        answers.add(nine);

        ArrayList<Question> questions10=new ArrayList<>();
        questions10.add(new Question("There are two different wallets available for channel partners one is the" +
                " lfyd’s commission wallet that’s exclusive for merchants where the partners can recharge their wallet and" +
                " a specific commission is deducted from them whenever a customers purchase or avail any services through our " +
                "app  the next one is the lfyd’s wallet which is common for all where the merchants will receive some of the revenue" +
                " from sales which they can use to recharge their commission wallet or buy any products from other channel partners."));
        Answer ten=new Answer("Why are there two wallets in my app ?",questions10);
        answers.add(ten);

        ArrayList<Question> questions11=new ArrayList<>();
        questions11.add(new Question("To bring customers to our channel partners our company pays or spends some money for " +
                " the customers which they give in form of cashback, this cashback is actually going in their wallet which they  ultimately " +
                "use in paying to any of our channels partners so the wallet money can be exchanged for the equivalent rupee."));
        Answer eleven=new Answer("How lfyd’s wallet works ?",questions11);
        answers.add(eleven);

        ArrayList<Question> questions12=new ArrayList<>();
        questions12.add(new Question("Merchants can easily use their rupee in lfyd’s wallet to purchase any product " +
                "from any channel partner stores with no problem and other than that we know our partner prefer direct account" +
                " transfer or cash for sales so we have kept the maximum pa from wallet to be only 25% of the total price of services or product ."));
        Answer twelve=new Answer("Why should I accept a few of my earning generated in the wallet ?",questions12);
        answers.add(twelve);

        ArrayList<Question> questions13=new ArrayList<>();
        questions13.add(new Question("with lfyd, you are not just any other shop you become our channel partner we give you extra privileges by " +
                "giving you exclusive partner discounts at a no of stores other than that we give you more access to your wallet."));
        Answer thirteen=new Answer("Other than getting customers what extra benefits I get ?",questions13);
        answers.add(thirteen);

        ArrayList<Question> questions14=new ArrayList<>();
        questions14.add(new Question("Customer feedback is one of the most important and we take it very seriously, so if you want to give any feedback " +
                "about our app, our startup or anything in common please open the feedback section from the app drawer and tell us whatever you want, you can also" +
                " mail us at support@lfydin."));
        Answer fourteen=new Answer("If I need to give some idea about the app where can I do that ?",questions14);
        answers.add(fourteen);

        QuestionAdapter adapter= new QuestionAdapter(answers);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentList = new Intent(getApplicationContext(), ProfileActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentList);
        overridePendingTransition(0,0);
        super.onBackPressed();

    }
}
