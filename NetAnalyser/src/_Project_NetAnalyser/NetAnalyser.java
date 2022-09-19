package _Project_NetAnalyser;

import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *  Title: NetAnalyser.java
 *  Description: This class is  a graphical user interface (GUI) application to execute the fundamental
 *               network diagnostic tool ping1and display its output together with a histogram of
 *               Round Trip Time (RTT) values.
 *  @author  Sam Wang
 *  @version 1.0
 */

public class NetAnalyser {
    public static void main(String[] args) {
        // Declaration of the new JFrame Frame;
        JFrame Frame = new JFrame();
        // Declaration of the ComboBox number and set the value 1-10;
        String [] boxNo = {"1","2","3","4","5","6","7","8","9","10"};

        // Initialize the JLabel, JButton, JTextField, JTextArea and JComboBox.
        // Set their text and bounds;
        JLabel LabelE = new JLabel("Enter Test URL & no. of probes and click on Process");
        LabelE.setBounds(10,10,500,20);

        JLabel LabelT = new JLabel("Test URL");
        LabelT.setBounds(10,105,100,20);

        JLabel LabelNo = new JLabel("No. of probes");
        LabelNo.setBounds(10,200,100,20);

        JLabel LabelH = new JLabel("Histogram");
        LabelH.setBounds(925,10,100,20);

        JLabel LabelHO = new JLabel();
        LabelHO.setBounds(875,-50,400,400);

        JButton ButtonP = new JButton("Process");
        ButtonP.setBounds(120,280,100,20);

        JTextField TextURL = new JTextField();
        TextURL.setBounds(80,105,200,20);

        JTextArea AreaPing = new JTextArea("Your output will apper here.");
        AreaPing.setBounds(525,10,143,20);

        JComboBox <String> ComboBoxNo = new JComboBox <> (boxNo);
        ComboBoxNo.setSelectedIndex(0);
        ComboBoxNo.setBounds(120,205,50,20);

        // Initialize the Frame;
        Frame.setTitle("NetAnalyser V1.0");
        Frame.setLayout(null);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(1200,400);
        Frame.setVisible(true);
        // Adding the elements into the Frame;
        Frame.add(LabelE);
        Frame.add(LabelT);
        Frame.add(LabelNo);
        Frame.add(LabelH);
        Frame.add(ButtonP);
        Frame.add(TextURL);
        Frame.add(AreaPing);
        Frame.add(ComboBoxNo);
        //Adding the action listener e to the "Process" button.
        ButtonP.addActionListener(e -> {
            //URL: Receive the URL user typed in;
            String URL = TextURL.getText();
            //ipAddress: Set the standard format "ping -n" format + how many times + URL;
            int num = ComboBoxNo.getSelectedIndex() + 1;
            String Select = String.valueOf(num);
            String ipAddress = "ping -n "+ Select + " " + URL;
            try {
                //Once the user click the "Process" button, the AreaPing and LabelH will change the bounds;
                //Call "Ping" method.
                //Then the AreaPing will change the text to Ping(ipAddress);
                AreaPing.setBounds(425, 10, 400, 320);
                LabelH.setBounds(985,10,100,20);
                AreaPing.setText(ping03(ipAddress));

            } catch (Exception e1) {
                //Catch the exception and print.
                e1.printStackTrace();
            }
            //Show the result of the histogram;
            LabelHO.setText(Histogram(AreaPing.getText()));

        });
        Frame.add(LabelHO);
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
    public static String ping03(String ipAddress) throws Exception {
        //Create new BufferedReader
        BufferedReader myBr;
        // Initialize the process;
        Process p = Runtime.getRuntime().exec(ipAddress);
        myBr = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //Create new String.
        String line;
        // Create new StringBuilder.
        StringBuilder mySb=new StringBuilder();
        //read the whole process of the Ping method until the last line;
        while ((line = myBr.readLine()) != null) {
            //get /n;
            mySb.append(line).append("\n");
        }
        //Return the result: Ping method output.
        return mySb.toString();
    }

    /**
     * Title: Histogram.
     * Description: "Histogram" method gets the information from the output of "Ping"method
     *              and return the histogram information
     * @param b: Get the ping result and find the information in it.
     * @author Sam Wang
     * @return java.lang.String
     */
    public static String Histogram(String b) {
        // the sequence of each RTT result information list.
        int i,j;
        int record=0;
        // Store every RTT result of each probe.
        int[] RTT = new int[10];
        for( i = 0; i < b.length(); i++)
        {
            // Get the RTT information: time(ms) by searching the output of "Ping" method
            // The system language should be Chinese: ".equals("¼ä")" or English: ".equals("e")";
            if((b.subSequence(i, i+1).equals("¼ä")|| b.subSequence(i, i+1).equals("e"))&& b.subSequence(i+1, i+2).equals("="))
            {
                for(j = i+2;j < i + 6;j++)
                {
                    //Get the RTT information by searching the output of "Ping" method
                    //The system language should be Chinese or English;
                    if(b.startsWith("m", j))
                    {
                        // Change the RTT from string type into integer type.
                        RTT[record]=Integer.parseInt(b.substring(i+2, j));
                        record++;

                        j=i+7;
                    }
                }
            }
        }
        // Create a variable for the maximum RTT;
        // Create a variable for the minimum RTT;
        double MAX=RTT[0];
        double MIN=RTT[0];
        for(i = 0;i < RTT.length;i++)
        {
            //For loop find the maximum RTT;
            if(MAX<RTT[i])
                MAX=RTT[i];
        }

        for(i = 0;i < RTT.length;i++)
        {
            //For loop find the minimum RTT;
            if(MIN > RTT[i] && RTT[i] != 0)
                MIN = RTT[i];
        }
        //Create variables for recording the numbers of RTT in the three bins;
        double BIN =(MAX - MIN) / 3;
        int recordbin1 = 0;
        int recordbin2 = 0;
        int recordbin3 = 0;

        //Get the RTTs into the correct bins;
        for( i = 0;i < RTT.length;i++)
        {
            if( RTT[i] >= MIN && RTT[i] < MIN + BIN)
                recordbin1++;
            if( RTT[i] >= MIN + BIN && RTT[i] < MIN + 2 * BIN)
                recordbin2++;
            if( RTT[i] >= MIN + 2*BIN && RTT[i] <= MAX)
                recordbin3++;
        }

        String min =String.valueOf(MIN);
        String max =String.valueOf(MAX);

        BigDecimal bg1 = new BigDecimal(MIN + BIN);
        double min1bin = bg1.setScale(1, RoundingMode.HALF_UP).doubleValue();
        BigDecimal bg2 = new BigDecimal(MIN+2*BIN);
        double min2bin = bg2.setScale(1, RoundingMode.HALF_UP).doubleValue();

        // Initialize three bins' information of histogram;
        StringBuilder thisOutPut1 = new StringBuilder(min + "&lt;=" + "RTT" + "&lt;" + min1bin + "&emsp;");
        StringBuilder thisOutPut2 = new StringBuilder(min1bin + "&lt;=" + "RTT" + "&lt;" + min2bin + "&emsp;");
        StringBuilder thisOutPut3 = new StringBuilder(min2bin + "&lt;=" + "RTT" + "&lt;=" + max + "&ensp;");

        // Add X to different bins
        for(i = 0 ; i < 3 ; i ++)
        {
            if (i == 0)
            {
                for(j = 0;j < recordbin1;j ++)
                {
                    thisOutPut1.append("  X  ");
                }
            }
            if(i==1)
            {
                for(j = 0;j < recordbin2;j ++)
                {
                    thisOutPut2.append("  X  ");
                }
            }
            if(i == 2)
            {
                for(j = 0;j < recordbin3;j ++)
                {
                    thisOutPut3.append("  X  ");
                }
            }
        }
        // HistogramOutPut is the String type to return the result;
        String HistogramOutPut = "<html><body>" + thisOutPut1 + "<br>" + "<br>" + thisOutPut2
                + "<br>" + "<br>" + thisOutPut3 + "<br>" + "<body></html>";
        //If the RTTs are all the same, just have one line;
        if(BIN == 0)
            HistogramOutPut = "<html><body>" + thisOutPut3 + "<br>" + "<body></html>";
        //return the HistogramOutPut.
        return HistogramOutPut;
    }
}