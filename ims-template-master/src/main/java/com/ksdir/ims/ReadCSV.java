package com.ksdir.ims;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

public class ReadCSV 
{
    public static double[] x;
    public static double[] y;
    public static double[][] centy;
    public static double[][] centx;
    
	public static void main(String[] args)
	{
		String file = "C:\\Users\\Tobiasz\\EclipseProjects\\ims-template-master\\doc\\wifigdansk.csv";
		List<double[]> myListofXY = new ArrayList<double[]>();
		myListofXY = Read(file);
        x = myListofXY.get(0);
        y = myListofXY.get(1);
		drawPlot(y, x);
		
		int k = 3;
		double centroidy[][]=new double[][]{
			{0, 0, 0},
			{18.5, 18.6, 18.7}
		};
		double centroidx[][]=new double[][]{
			{0, 0, 0},
			{54.36, 54.38, 54.4}
		};
		
		System.out.println("In case of y");
		centy = getCentroid(y, k, centroidy);
		System.out.println("In case of x");
		centx = getCentroid(x, k, centroidx);
		
		System.out.println("Center of firts region: " + centx[1][0] + ", " + centy[1][0]);
		System.out.println("Center of second region: " + centx[1][1] + ", " + centy[1][1]);
		System.out.println("Center of third region: " + centx[1][2] + ", " + centy[1][2]);
		
	}
	
	public static List<double[]> Read(String file)
	{
		BufferedReader reader = null;
		List<double[]> myList = new ArrayList<double[]>();
		try
		{
			List<Runner> hotspots = new ArrayList<Runner>();		
			String row = "";
			reader = new BufferedReader(new FileReader(file));
			reader.readLine();
			
			while((row = reader.readLine()) != null)
			{
				String[]  data = row.split(",");
				
				if(data.length > 0)
				{
					Runner hotspot = new Runner();
					hotspot.setID(data[0]);
					hotspot.setLocation(data[1]);
					hotspot.setX(Double.parseDouble(data[2]));
					hotspot.setY(Double.parseDouble(data[3]));
					hotspots.add(hotspot);
				}
			}
			
			int i = 0;
			double[] gx = new double[100]; ;
			double[] gy = new double[100];;
			
			for(Runner hotspot : hotspots)
			{
				gx[i] = Double.valueOf(hotspot.getX());	
				gy[i] = Double.valueOf(hotspot.getY());
				i++;		
			}
			
			myList.add(gx);
			myList.add(gy);		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(Exception except)
			{
				except.printStackTrace();
			}
		}	
		
		return myList;
	}
	
	public static double[][] getCentroid(double data[], int k, double centroid[][])
	{
		double distance[][] = new double[k][data.length];
		double cluster[] = new double[data.length];
		int clusternodecount[] = new int[k];		
		centroid[0] = centroid[1];
		centroid[1] = new double[]{0,0,0};

		for(int i = 0; i < k; i++)
		{
			for(int j = 0; j < data.length; j++)
			{
				distance[i][j] = Math.abs(data[j] - centroid[0][i]);
			}
		}
		
		for(int j = 0; j < data.length; j++)
		{
			int smallerDistance = 0;
			if(distance[0][j] < distance[1][j] && distance[0][j] < distance[2][j])
				smallerDistance = 0;
			if(distance[1][j] < distance[0][j] && distance[1][j] < distance[2][j])
				smallerDistance = 1;
			if(distance[2][j] < distance[0][j] && distance[2][j] < distance[1][j])
				smallerDistance = 2;
			centroid[1][smallerDistance] = centroid[1][smallerDistance] + data[j];
			clusternodecount[smallerDistance] = clusternodecount[smallerDistance] + 1;
			cluster[j] = smallerDistance; 
		}
		
		for(int j = 0; j < k; j++)
		{
			centroid[1][j] = centroid[1][j]/clusternodecount[j];
		}

		boolean isAchived = true;
		
		for(int j = 0; j < k; j++)
		{
			if(isAchived && centroid[0][j] == centroid[1][j])
			{
				isAchived = true;
				continue;
			}
			
			isAchived = false;
		}
		
		if(!isAchived)
		{             
			getCentroid(data, k, centroid);
		}
		
		if(isAchived)
		{	
			System.out.println("======================================== ");
			System.out.println(" ");
			System.out.println("Final cordnate is");
			
			for(int i = 0; i < k; i++)
			{	
                System.out.print("Region " + (i + 1) + ": ");
                int a = 0; 
                
				for(int j = 0; j < data.length; j++)
				{
					if(cluster[j] == i)
					{
						System.out.print(data[j] + ", ");
						a++;
					}
				}
				
				System.out.println("");
				System.out.print("Mean value of hotspots is " + a);
				System.out.println("");
			}
			
			System.out.println("");
		}
		
		return centroid;	
	}
	
    private static void drawPlot(double[] x, double[] y) {
        Plot2DPanel plot = new Plot2DPanel();
        plot.addScatterPlot("Location", x, y);
        JFrame frame = new JFrame("Wifi Location");
        frame.setSize(900, 900);
        frame.setContentPane(plot);
        frame.setVisible(true);
    }
}
