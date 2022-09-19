package _Project_NetAnalyser;

import java.io.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

/**
 *  Title: NetAnalyser.java
 *  Description: This class is the version for the Task2. Including 3 new methods:
 *               FileContent, FileSave and TitleChange and some changes in the previous methods.
 *  @author  Sam Wang
 *  @version 1.1
 */

public class NetAnalyserV1_1 {
        public static void main(String[] args) {
            // Declaration of the input max number of probes for args[0];
            int size = Integer.parseInt(args[0]);
            JFrame.setDefaultLookAndFeelDecorated(true);
            // Declaration of the new JFrame Frame;
            JFrame Frame = new JFrame();
            // Declaration of the ComboBox number;
            String [] boxNo = new String [size];
            for (int i = 0; i < Integer.parseInt(args[0]) ; i++) {
                // Initialize the boxNo according to the args[0];
                boxNo[i] = String.valueOf(i+1);
            }
            // Initialize the JLabel, JButton, JTextField, JTextArea and JComboBox.
            // Set their text and bounds;
            JLabel LabelE = new JLabel("Enter Test URL & no. of probes and click on Process");
            LabelE.setBounds(10,10,400,20);
            JLabel LabelT = new JLabel("Test URL");LabelT.setBounds(10,105,100,20);
            JLabel LabelNo = new JLabel("No. of probes");LabelNo.setBounds(10,200,100,20);
            JLabel LabelH = new JLabel("Histogram");LabelH.setBounds(925,10,100,20);
            JLabel LabelHistogram = new JLabel();LabelHistogram.setBounds(850,-50,400,400);
            JButton ButtonP = new JButton("Process");ButtonP.setBounds(120,280,100,20);
            JTextField TextURL = new JTextField();TextURL.setBounds(80,105,200,20);
            JTextArea AreaPing = new JTextArea("Your output will appear here.");
            AreaPing.setBounds(525,10,145,20);

            JComboBox<String> ComboBoxNo = new JComboBox<>(boxNo);
            ComboBoxNo.setSelectedIndex(0);// Set the default number of the ComboBox;
            ComboBoxNo.setBounds(120,205,50,20);

            // Initialize the Frame;
            Frame.setTitle("NetAnalyser V1.1");
            Frame.setLayout(null);
            Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Frame.setSize(1200,600);
            Frame.setVisible(true);
            // Adding the elements into the Frame;
            Frame.add(LabelE);Frame.add(LabelT);Frame.add(LabelNo);Frame.add(LabelH);
            Frame.add(ButtonP);Frame.add(TextURL);Frame.add(AreaPing);Frame.add(ComboBoxNo);
            //Adding the action listener e to the "Process" button.
            ButtonP.addActionListener(e -> {

                //URL: Receive the URL user typed in;
                String URL = TextURL.getText();
                //ipAddress: Set the standard format "ping -n" format + how many times + URL;
                String ipAddress = "ping -n " + (ComboBoxNo.getSelectedIndex() + 1) + " " + URL;
                try {
                    //Once the user click the "Process" button, the AreaPing and LabelH will change the bounds;
                    //Call "Ping" method.
                    //Then the AreaPing will change the text to Ping(ipAddress);
                    AreaPing.setBounds(425, 10, 400, 520);
                    LabelH.setBounds(985,10,100,20);
                    AreaPing.setText(Ping(ipAddress));
                } catch (Exception e1) {

                    //Catch the exception and print.
                    e1.printStackTrace();
                }
                //Save the file title.
                String FileTitle = TitleChange(URL);
                try {
                    //Change the histogram.
                    LabelHistogram.setText(Histogram(AreaPing.getText(),size,FileTitle));
                } catch (Throwable e1) {
                    //Catch the exception and print.
                    e1.printStackTrace();
                }

            });
            //Show the result of the histogram;
            Frame.add(LabelHistogram);
        }

    /**
     * Title: Ping
     * Description: Ping method means to call the Ping method and return the String result.
     * @param ipAddress: String, consist of the "ping -n" and the how many times and URL.
     *                  "ping -n " + (ComboBoxNo.getSelectedIndex() + 1) + " " + URL;
     * @return java.lang.String
     * @throws IOException
     * @author Sam Wang
     */
        public static String Ping(String ipAddress) throws IOException {
            //Create new BufferedReader
            BufferedReader br;
            // Initialize the process;
            Process p = Runtime.getRuntime().exec(ipAddress);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //Create new String.
            String a;
            // Create new StringBuilder.
            StringBuilder stringBuilder = new StringBuilder();
            //read the whole process of the Ping method until the last line;
            while ((a = br.readLine()) != null) {
                //get /n;
                stringBuilder.append(a).append("\n");
            }
            //Return the result: Ping method output.
            return stringBuilder.toString();
        }

    /**
     * Title: Histogram.
     * Description: "Histogram" method gets the information from the output of "Ping"method
     *              and return the histogram information
     * @param pingResult: Get the ping result and find the information in it.
     * @param size: the size of the ComboBox, which is the number of args[0];
     * @param Title: the Title of the new created file. FileTitle = TitleChange(URL);
     * @return java.lang.String
     * @throws Exception
     * @author Sam Wang
     */
        public static String Histogram(String pingResult, int size, String Title) throws Exception {
            // the sequence of each RTT result information list.
            int record=0;
            // Store every RTT result of each probe.
            int[] RTT = new int[size];
            for(int i=0; i< pingResult.length(); i++)
            {
                // Get the RTT information: time(ms) by searching the output of "Ping" method
                // The system language should be Chinese: ".equals("¼ä")" or English: ".equals("e")";
                if((pingResult.subSequence(i, i+1).equals("¼ä")|| pingResult.subSequence(i, i+1).equals("e"))
                        &&pingResult.subSequence(i+1, i+2).equals("="))
                {
                    for(int j=i+2;j<i+6;j++)
                    {
                        //Get the RTT information by searching the output of "Ping" method
                        //The system language should be Chinese or English;
                        if(pingResult.startsWith("m", j))
                        {
                            // Change the RTT from string type into integer type.
                            RTT[record]=Integer.parseInt(pingResult.substring(i+2, j));
                            record++;
                            j=i+7;
                        }
                    }
                }
            }
            // Create a variable for the maximum RTT;
            // Create a variable for the minimum RTT;
            double MAX = RTT[0];
            double MIN = RTT[0];
            for(int i = 0; i < RTT.length; i++)
            {
                //For loop find the maximum RTT;
                if(MAX < RTT[i]) MAX = RTT[i];
            }
                //For loop find the minimum RTT;
            for(int i=0;i<RTT.length;i++)
            {
                if(MIN>RTT[i]&&RTT[i]!=0) MIN=RTT[i];
            }

            //Create variables for recording the numbers of RTT in the three bins;
            double BIN = (MAX-MIN)/3;// Set the separate symbol;
            int record1=0;
            int record2=0;
            int record3=0;

            //Get the RTTs into the correct bins;
            for (int value : RTT) {
                if (value >= MIN && value < MIN + BIN)
                    record1++;
                if (value >= MIN + BIN && value < MIN + 2 * BIN)
                    record2++;
                if (value >= MIN + 2 * BIN && value <= MAX)
                    record3++;
            }

            // Call "File_Content" method to get the content of the histogram file;
            String Content= File_Content(MIN, MAX, BIN, record1, record2, record3);
            File_Save(Title, Content);

            BigDecimal bg1 = new BigDecimal(MIN+BIN);
            double min1bin = bg1.setScale(1, RoundingMode.HALF_UP).doubleValue(); // Keep a decimal.
            BigDecimal bg2 = new BigDecimal(MIN+2*BIN);
            double min2bin = bg2.setScale(1, RoundingMode.HALF_UP).doubleValue(); // Keep a decimal.

            // Initialize three bins' information of histogram;
            StringBuilder thisOutPut1 = new StringBuilder(MIN + "&lt;=" + "RTT" + "&lt;" + min1bin + "&emsp;");
            StringBuilder thisOutPut2 = new StringBuilder(min1bin + "&lt;=" + "RTT" + "&lt;" + min2bin + "&emsp;");
            StringBuilder thisOutPut3 = new StringBuilder(min2bin + "&lt;=" + "RTT" + "&lt;=" + MAX + "&ensp;");

            // Add X to different bins.
            thisOutPut1.append(" X ".repeat(Math.max(0, record1)));
            thisOutPut2.append(" X ".repeat(Math.max(0, record2)));
            thisOutPut3.append(" X ".repeat(Math.max(0, record3)));

            // HistogramOutPut is the String type to return the result;
            String HistogramOutPut ="<html><body>"+thisOutPut1+"<br>"+"<br>"
                    +thisOutPut2+"<br>"+"<br>"+thisOutPut3+"<br>"+"<body></html>" ;
            //If the RTTs are all the same, just have one line;
            if(BIN==0)
                HistogramOutPut ="<html><body>"+thisOutPut3+"<br>"+"<body></html>";
            //return the HistogramOutPut.
            return HistogramOutPut;
        }

    /**
     * Title: TitleChange.
     * Description: "TitleChange" method aims to get the String URL and change the format
     *              then return the proper text file title.
     * @param url: The URL in the previous String type variable, which is the website user typed in.
     *             String URL = TextURL.getText();
     * @return java.lang.String
     * @author Sam Wang
     */
        public static String TitleChange(String url) {
            // Create title variable
            StringBuilder title = null;
            // Change the title into the required format
            for(int i=1;i<url.length();i++) {
                if(i==1)
                    title = new StringBuilder(url.substring(i - 1, i));
                if(url.startsWith(".", i))
                {
                    title.append("-");
                }
                else
                {
                    title.append(url, i, i + 1);
                }
            }
            // Setting time variables
            String YEAR, MONTH, DATE, HOUR, MINUTE, SECOND;
            Calendar now;
            now = Calendar.getInstance();
            YEAR= String.valueOf(now.get(Calendar.YEAR));
            MONTH= String.valueOf(now.get(Calendar.MONTH)+1);
            DATE= String.valueOf(now.get(Calendar.DATE));
            HOUR= String.valueOf(now.get(Calendar.HOUR_OF_DAY));
            MINUTE= String.valueOf(now.get(Calendar.MINUTE));
            SECOND= String.valueOf(now.get(Calendar.SECOND));
            assert title != null;
            //Get the formal title.
            title.append("-").append(YEAR).append("-").append(MONTH).append("-").append(DATE).
                    append("-").append(HOUR).append("-").append(MINUTE).append("-").append(SECOND);

            return title.toString();
        }

    /**
     * Title: File_Content.
     * Description: "FileContent" method is in the "Histogram" method and it aims to get the information
     *               from "Histogram" method and return the text file content.
     * @param MIN: The variable for the minimum RTT;
     * @param MAX: The variable for the maximum RTT;
     * @param BIN: The separate symbol for the different types of RTTs;
     *             double BIN = (MAX-MIN)/3;
     * @param recordBin1: Variable for recording the numbers of RTTs in the first class, the fast parts.
     * @param recordBin2: Variable for recording the numbers of RTTs in the second class, the middle parts.
     * @param recordBin3: Variable for recording the numbers of RTTs in the third class, the slow parts.
     * @return java.lang.String
     * @author Sam Wang
     */
        public static String File_Content(double MIN, double MAX, double BIN, int recordBin1, int recordBin2, int recordBin3) {

            String Content = "RTT(ms) histogram"+"\n";
            String Transfer;
            BigDecimal bg1 = new BigDecimal(MIN + BIN);
            double min1bin = bg1.setScale(1, RoundingMode.HALF_UP).doubleValue();
            BigDecimal bg2 = new BigDecimal(MIN + 2*BIN);
            double min2bin = bg2.setScale(1, RoundingMode.HALF_UP).doubleValue();
            if (BIN != 0) {
                Transfer = MIN + "-" + min1bin + ":" + recordBin1 + "\n";
                Content = Content + Transfer;
                Transfer = min1bin + "-" + min2bin + ":" + recordBin2 + "\n";
                Content = Content + Transfer;
            }
            Transfer = min2bin +"-"+ MAX +":"+ recordBin3 +"\n";
            Content = Content + Transfer;

            return Content;
        }

    /**
     * Title: File_Save.
     * Description: "File_Save" method is in the "Histogram" method
     *              which aims to save the information from Histogram into the file.
     * @param Title:the Title of the new created file. FileTitle = TitleChange(URL);
     * @param Content: The content from the "File_Content" method.
     *                 String Content= File_Content(MIN, MAX, BIN, record1, record2, record3);
     * @throws IOException
     * @author Sam Wang
     */
        public static void File_Save(String Title, String Content) throws IOException {

            // Create a new boolean to verify is new file or not.
            //File name;
            Title = Title + ".txt";
            Content = Title + "\n" + "\n" + Content;
            // Relative path,if it do not exist,then create a new output ".txt" file;
            FileWriter writer = new FileWriter(Title);
            try{
                // create a new file
                BufferedWriter out = new BufferedWriter(writer);
                out.write(Content);
                // Flush the cache into the file;
                out.flush();
                // File close.
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

