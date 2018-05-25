package com;


import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.*;


import com.swetake.util.Qrcode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**get All Files in a dir, exclude sub-directory*/


public class QRcode {
	public static void QRcodeGen(String str,String filepath,String filename)
	{
        //�����ά��ͼƬ�ĸ߿��
        // API�ĵ��涨����ͼƬ��ߵķ�ʽ ��v�Ǳ��β��Եİ汾��
        int v =9;
        int width = 67 + 12 * (v - 1);
        int height = 67 + 12 * (v - 1);

        Qrcode x = new Qrcode();
        /**
         * ����ȼ���Ϊ
         * level L : ��� 7% �Ĵ����ܹ���������
         * level M : ��� 15% �Ĵ����ܹ���������
         * level Q : ��� 25% �Ĵ����ܹ���������
         * level H : ��� 30% �Ĵ����ܹ���������
         */
        x.setQrcodeErrorCorrect('H');
        x.setQrcodeEncodeMode('B');//ע��汾��Ϣ N�������� ��A���� a-z,A-Z��B���� ����)
        x.setQrcodeVersion(v);//�汾��  1-40
        String qrData = str;//������Ϣ
        byte[]d = qrData.getBytes();//����ת��ʽ��Ҫ�׳��쳣
        //������
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        //��ͼ
        Graphics2D gs = bufferedImage.createGraphics();

        gs.setBackground(Color.WHITE);
        gs.setColor(Color.BLACK);
        gs.clearRect(0, 0, width, height);

        //ƫ����
        int pixoff = 2;


        /**
         * ���ײȿӵĵط�
         * 1.ע��forѭ�������i��j��˳��
         *   s[j][i]��ά�����j��i��˳��Ҫ����������е� gs.fillRect(j*3+pixoff,i*3+pixoff, 3, 3);
         *   ˳��ƥ�䣬�������ֽ���ͼƬ��һ������
         * 2.ע����ж�if (d.length > 0 && d.length < 120)
         *   �Ƿ�������ַ������ȴ���120�������ɴ��벻ִ�У���ά��հ�
         *   �����Լ����ַ�����С�����ô�����
         */
        if (d.length > 0 && d.length < 120) {
            boolean[][] s = x.calQrcode(d);

            for (int i = 0; i < s.length; i++) {
                for (int j = 0; j < s.length; j++) {
                    if (s[j][i]) {
                        gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                    }
                }
            }
        }
        gs.dispose();
        bufferedImage.flush();
        //����ͼƬ��ʽ���������·��
        try {
        	ImageIO.write(bufferedImage, "png", new File(filepath+"\\"+filename+".png"));
        }catch (IOException ex) {System.out.println(ex.toString());;}
        //System.out.println("��ά���������");
	}
	
	public static void main(String[] args) 
	{
		Qrcode x = new Qrcode();
		String path = "H:\\";
		String name = "abc";
		QRcodeGen("abc",path,name);
	}
 
	


}
