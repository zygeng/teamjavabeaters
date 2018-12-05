import java.util.*;
import java.io.File;

/**
This shows how to read an excel file as a 2d array of Strings
*/
public class JavaBeaterDemo{
  public static void main(String[] args){
    String[][] nasdaqData = readData();
    System.out.println("Please enter the name of the firm:");
    String firmName = TextIO.getln();
    String[][] firm = readFirm(nasdaqData, firmName);
    writeData(firm);
    String[][] timeSpecified=chooseTimePeriod(firm);
    printCompanyInTime(timeSpecified);
    double currentratio = calculateCurrentRatio(timeSpecified);
    double allratio = calculateAllCurrentRatio(firm);
    System.out.println("Do you want further analyze your data? (Yes or No) ");
    String answer = TextIO.getln();
    if(answer.equals("Yes")){
      double n = calculateCurrentRatio(timeSpecified)-calculateAllCurrentRatio(firm);
      if (n>0) {
        System.out.printf("Congratulations! The ratio of your company is above the average ratio! The average ratio is %.2f.%n",allratio);
      }else{
        System.out.printf("Please keep increase your assets and try to reduce liabilities in the future!%n");
      }
    }
    else{
      System.out.printf("Do you want to compare with yourself instead?(Yes or No)%n");
      String answer2 = TextIO.getln();
      if (answer2.equals("Yes")) {
        System.out.printf("Enter a timeperiod that you want to compare with:%n");
        String[][] timeSpecified2 = chooseTimePeriod(firm);
        printCompanyInTime(timeSpecified2);
        double m = calculateCurrentRatio(timeSpecified2)- (calculateCurrentRatio(timeSpecified2)-calculateAllCurrentRatio(firm));
        if (m>0) {
          System.out.printf("Your current ratio is better than the selected!%n");
        }
        else{
          System.out.printf("Your current ratio is worse than the selected!%n");
        }
      }
      else {
        System.out.printf("The current ratio of this company is %.2f during this time period.%n",currentratio);
        if(currentratio>1){
          System.out.printf("Since the currentratio is greater than 1, it showsthat the company has a great liquidity and it is capable of handling unforeseeable contingencies that may arise in the short run.%n");
        }else{
          System.out.printf("This company has a low liquidity. It shows that the current of this company cannot be covered by the current assets.%n");
        }
      }
    }
  }

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

    public static void writeData(String[][] data){
      String[] cols =
      {"period","company","tickers","Indicator","amount"};
      for(int i=0; i<data.length; i++){
        System.out.printf("%5d ",i);
        for(int j=0 ; j<data[i].length ; j++){
          System.out.printf("%10s ",data[i][j]);
        }
        System.out.println();
      }
    }

    public static String[][] chooseTimePeriod(String[][] data){
      System.out.println("Please enter the time period for which you want the data (e.g., 2014 Q3)");
      String time=TextIO.getln();
      int count=0;
      for (int i=0;i<data.length;i++){
        if (data[i][0].equals(time)){
          count++;
        }
      }
      String[][] companyInTime=new String[count][5];
      int row=0;
      for (int i=0;i<data.length;i++){
        if (data[i][0].equals(time)){
          for(int x=0;x<5;x++){
            companyInTime[row][x]=data[i][x];
          }
          row++;
        }
      }
      return companyInTime;
    }

    public static void printCompanyInTime(String[][] timeSpecified){
      for (int i=0;i<timeSpecified.length;i++){
        for (int x=0;x<timeSpecified[i].length; x++){
          System.out.print(i+" ");
          System.out.print(timeSpecified[i][x]+" ");
        }
        System.out.println();
      }
    }

    public static double calculateCurrentRatio(String[][] timeSpecified){
      double assets = 0;
      double tle = 0;
      double te = 0;
      double liabilities = 0;
      double currentratio = 0;
      for(int i = 0; i<timeSpecified.length; i++){
        if(timeSpecified[i][3].equals("Assets")){
          assets = Double.valueOf(timeSpecified[i][4]);
        }else if(timeSpecified[i][3].equals("Total Liabilities and Equity")){
          tle = Double.valueOf(timeSpecified[i][4]);
        }else if(timeSpecified[i][3].equals("Total Equity")){
          te = Double.valueOf(timeSpecified[i][4]);
        }
        liabilities = tle - te;
        currentratio = assets/liabilities;
      }
      return currentratio;
    }

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
  }
