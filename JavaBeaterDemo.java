import java.util.*;
import java.io.File;

/**
* This project will read data from the FinancialData.csv document
* and ask the user for the company's name and the period of time that they want to analyze
* customers can analyze the financial data in that period of time in this project
* they can also compare with the same company's data in different period of time
* and we also provide advices for the company based on the financial data
* The authors of this project are:
* @author Wentao Li
* @author Zida Wang
* @author Zeyu Geng
* @author Dingyuan He
* @author Yuchen Ge
*/

public class JavaBeaterDemo{
  public static void main(String[] args){
    String[][] nasdaqData = readData(); // readData is a method from Line 85

    System.out.println("Please enter the name of the firm:");
    String firmName = TextIO.getln();
    String[][] firm = readFirm(nasdaqData, firmName); // readFirm is a method from Line 115
    //the financial data of that company will be stored in the array named firm

    writeData(firm); //writeData is a method from Line 145

    String[][] timeSpecified=chooseTimePeriod(firm); //chooseTimePeriod is a method from Line 165
    printCompanyInTime(timeSpecified); // printCompanyInTime is a method from Line 195
    double currentratio = calculateCurrentRatio(timeSpecified); //calculateCurrentRatio is a method from Line 213
    double allratio = calculateAllCurrentRatio(firm); //calculateAllCurrentRatio is a method from Line 243

    System.out.println("Do you want further analyze your data? (Yes or No) ");
    String answer = TextIO.getln();
    if(answer.equals("Yes")){
      double n = calculateCurrentRatio(timeSpecified)-calculateAllCurrentRatio(firm);
      // if the answer is "Yes", calculate the ratio.
      if (n>0) {
        System.out.printf("Congratulations! The ratio of your company is above the average ratio! The average ratio is %.2f.%n",allratio);
        //The average ratio is 0 and give the comments about the performance of company in the chosen period of time.
      }else{
        System.out.printf("Please keep increase your assets and try to reduce liabilities in the future!%n");
      }
      //if the company's ratio is below the average ratio, then print this line.
    }

    else{
      System.out.printf("Do you want to compare with yourself instead?(Yes or No)%n");
      String answer2 = TextIO.getln();
      if (answer2.equals("Yes")) {
        System.out.printf("Enter a timeperiod that you want to compare with:%n");
        String[][] timeSpecified2 = chooseTimePeriod(firm);
        //ask the customers the second period that they want to compare with, and store data in the 2D array named "timeSpecified2".
        printCompanyInTime(timeSpecified2);
        //use the method "printCompanyInTime" again to print out the data in the second period.
        double m = calculateCurrentRatio(timeSpecified2)- (calculateCurrentRatio(timeSpecified2)-calculateAllCurrentRatio(firm));
        //calculate the difference between two periods of time.
        if (m>0) {
          System.out.printf("Your current ratio is better than the selected!%n");
        }
        //the difference which is positive shows that this company performs better in the second period.
        else{
          System.out.printf("Your current ratio is worse than the selected!%n");
        }
        //the difference which is negative shows that this company performs worse in the second period.
      }

      else {
        //if the customers don't give any commands, then print out the current ratio because this is the project to provide the ratio of companies
        System.out.printf("The current ratio of this company is %.2f during this time period.%n",currentratio);
        if(currentratio>1){
          System.out.printf("Since the currentratio is greater than 1, it showsthat the company has a great liquidity and it is capable of handling unforeseeable contingencies that may arise in the short run.%n");
        }else{
          System.out.printf("This company has a low liquidity. It shows that the current of this company cannot be covered by the current assets.%n");
        }
      }
    }
  }

  /**
  * This method reads all the data in the file "FinancialData.csv".
  * and it returns data stored in array named "data"
  * this mathed shows how to read an excel file as a 2d array of Strings
  */
  public static String[][] readData(){
    //Date,Time,Transaction,Item
    String[][] data = new String[1112][6];
    try {
      String fileName = "FinancialData.csv"; //"BreadBasket_DMS.csv";
      File file = new File(fileName);

      // next create a scanner to read from the file
      Scanner fileScanner = new Scanner(file);

      // now read the entire file and print it with line numbers:
      int row=0;
      while (fileScanner.hasNextLine()) {
        String line = fileScanner.nextLine();
        data[row++]= line.split(",");
      }
      fileScanner.close();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return data;
  }

  /**
  * This method reads data of certain company that the user choose from the array "data".
  * and it returns array "company" in a 2d array of Strings
  * this mathed shows how to store the choosen data from a 2d array of Strings into a new array
  */
  public static String[][] readFirm(String[][] data, String name) {
    int counter = 0;
    for(int i = 0; i < data.length; i++) {
      {
        if (data[i][1].equals(name)) {
          counter++;
        }
      }
    }
    System.out.println(counter);
    String[][] company = new String[counter][5];
    int firmRow = 0;
    for(int i = 0; i < data.length; i++) {
      {
        if (data[i][1].equals(name)) {
          for(int j = 0; j < 5; j++) {
            company[firmRow][j] = data[i][j];
          }
          firmRow++;
        }
      }
    }
    System.out.println(data[1][1]);
    return company;
  }

  /**
  * This method prints out all the data stored in the array "firm"
  * and it shows how to print out each element in excel file as a 2d array of Strings
  */
  public static void writeData(String[][] data){
    String[] cols =
    {"period","company","tickers","Indicator","amount"};
    //there are five elements in each row.
    for(int i=0; i<data.length; i++){
      System.out.printf("%5d ",i);
      //number the lines of data
      for(int j=0 ; j<data[i].length ; j++){
        System.out.printf("%10s ",data[i][j]);
        //print out each element in array
      }
      System.out.println();
    }
  }

  /**
  * This method asks users to choose datas in a certain period of time
  * and then print out all the data in this certain period of time in the array "firm"
  * it shows how to filter data and find the specific data that we want in a 2d array of Strings
  */
  public static String[][] chooseTimePeriod(String[][] data){
    System.out.println("Please enter the time period for which you want the data (e.g., 2014 Q3)");
    String time=TextIO.getln();
    // ask for the time that users want
    int count=0;
    for (int i=0;i<data.length;i++){
      if (data[i][0].equals(time)){
        count++;
      }
    }
    //calculate how many lines of data in this certain period of time
    String[][] companyInTime=new String[count][5];
    // create a new array called "companyInTime" to store the data in certain period of time
    int row=0;
    for (int i=0;i<data.length;i++){
      if (data[i][0].equals(time)){
        for(int x=0;x<5;x++){
          companyInTime[row][x]=data[i][x];
        }
        row++;
      }
      //transfer the data in certain time into the array "companyInTime"
    }
    return companyInTime;
  }

  /**
  * This method prints out all the data stored in the array "timeSpecified"
  * it shows how to print out a 2d array of Strings
  */
  public static void printCompanyInTime(String[][] timeSpecified){
    for (int i=0;i<timeSpecified.length;i++){
      for (int x=0;x<timeSpecified[i].length; x++){
        System.out.print(i+" ");
        //print out which row of datas that is printed
        System.out.print(timeSpecified[i][x]+" ");
        //print out each data in the certain row
      }
      System.out.println();
    }
  }

  /**
  * This method calculates the ratio in specific period of tine of certain company that user choose
  * The function is Assets/Total Liabilities
  * and it returns currentratio
  * it shows how to transfer string in a 2d array of Strings into type double and then calculates the ratio
  */
  public static double calculateCurrentRatio(String[][] timeSpecified){
    double assets = 0;
    double tle = 0;
    double te = 0;
    double liabilities = 0;
    double currentratio = 0;

    for(int i = 0; i<timeSpecified.length; i++){
      if(timeSpecified[i][3].equals("Assets")){
        assets = Double.valueOf(timeSpecified[i][4]);
        //store company's assets from the array "timeSpecified" in double
      }else if(timeSpecified[i][3].equals("Total Liabilities and Equity")){
        tle = Double.valueOf(timeSpecified[i][4]);
        //store company's Total Liabilities and Equity from the array "timeSpecified" in double
      }else if(timeSpecified[i][3].equals("Total Equity")){
        te = Double.valueOf(timeSpecified[i][4]);
        //store company's Total Equity from the array "timeSpecified" in double
      }
      liabilities = tle - te;
      currentratio = assets/liabilities; //calculate the current ratio
    }
    return currentratio;
  }

  /**
  * This method calculates the ratio of all company
  * The function is Assets/Total Liabilities
  * and it returns allratio
  * it shows how to transfer string in a 2d array of Strings into type double and then calculates the ratio
  */
  public static double calculateAllCurrentRatio(String[][] firm){
      double assets = 0;
      double tle = 0;
      double te = 0;
      double liabilities = 0;
      double currentratio = 0;
      double allratio= 0;
      double sum1 = 0;
      double sum2 = 0;
      double sum3 = 0;
      for(int i = 0; i<firm.length; i++){
        if(firm[i][3].equals("Assets")){
          assets = Double.valueOf(firm[i][4]);
          sum1 = sum1 + assets;
        }else if(firm[i][3].equals("Total Liabilities and Equity")){
          tle = Double.valueOf(firm[i][4]);
          sum2 = sum2 + tle;
        }else if(firm[i][3].equals("Total Equity")){
          te = Double.valueOf(firm[i][4]);
          sum3 = sum3 + te;
        }
        liabilities = sum2 - sum3;
        allratio = sum1/liabilities;
      }
        return allratio;
    }
}
