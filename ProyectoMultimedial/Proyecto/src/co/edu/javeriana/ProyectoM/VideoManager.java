package co.edu.javeriana.ProyectoM;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaTool;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;

public class VideoManager {
	
	private String videoInput;
	private String videoOutput;
	private String audioOutput;
	private BufferedImage img;
	private IMediaReader reader;
	private final IMediaWriter writer;
	int index = 0;
	private int videoW;
	private int videoH;
	
	public VideoManager(String videoPath, String imgPath, String videoOutputP, String audioPath) {
		this.videoInput = videoPath;
		audioOutput = audioPath;
		this.videoOutput = videoOutputP;
		try {
			img = ImageIO.read(new File(imgPath));
		} catch (IOException evento) {
			evento.printStackTrace();
		}
		reader = ToolFactory.makeReader(videoInput.toString());
		reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		writer = ToolFactory.makeWriter(videoOutput, reader);
		dimensiones();
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, videoW, videoH);
		
	}
	
	public void imgEnVideo(float xIni, float yIni, float xEnd, float yEnd, int duration) {
		
		IMediaTool imt = new SIMT(img, xIni, yIni,xEnd, yEnd, duration);
		reader.addListener(imt);
	    //imt.addListener(writer);
		getAudioStream();
		while (reader.readPacket() == null);
		System.out.println("finsih");
		writer.close();
	}
	
	private class SIMT extends MediaToolAdapter {
		
		private BufferedImage image;
		private float x;
		private float y;
		private int d;
		private float xf;
		private float yf;
		private long inicio;
		
		public SIMT(BufferedImage image, float xIni, float yIni, float xEnd, float yEnd, int d) {
			this.image = image;
			this.x = xIni;
			this.y = yIni;
			this.d = d;
			this.xf = xEnd;
			this.yf = yEnd;
			this.inicio = (System.currentTimeMillis() / 1000);
			
		}
		
		@Override
		public void onVideoPicture(IVideoPictureEvent event) {
			BufferedImage imgFrame = event.getImage();
			int xr = (int) ((x*videoW)/100);
			int yr = (int) ((y*videoH)/100);
			int xfr = (int) ((xf*videoW)/100);
			int yfr = (int) ((yf*videoH)/100);
			
			int xInt = animate(xr, xfr, (System.currentTimeMillis() / 1000), inicio, d);
			int yInt = animate(yr, yfr, (System.currentTimeMillis() / 1000), inicio, d);
			
			if(xInt >= xfr && yInt >= yfr)
			{
				xInt = xfr;
				yInt = yfr;
			}
			
			BufferedImage fusion = imgEnImg(imgFrame, image, xInt, yInt);
			
			BufferedImage finalFrame = new BufferedImage(videoW, videoH, BufferedImage.TYPE_3BYTE_BGR);
			finalFrame.createGraphics().drawImage(fusion, 0, 0, null);
			writer.encodeVideo(0, finalFrame, (1000/24) * index++, TimeUnit.MILLISECONDS);
			
			writer.flush();
			super.onVideoPicture(event);
		}
		
		
	}
	
	public int animate(int posIni, int posFin, long actual, long inicio, long playTime)
	{
		int resp = 0;
		
		long diff = actual - inicio;
		
		double i = (double) diff / (double) playTime;
		
		resp = (int) (posIni + ((posFin - posIni) * i));
		
		return resp;
	}
	
	public BufferedImage imgEnImg(BufferedImage imgPrinc, BufferedImage imgSec, int posX, int posY)
	{
		int altoPrinc = imgPrinc.getHeight();
		int anchoPrinc = imgPrinc.getWidth();
		int altoSec = imgSec.getHeight();
		int anchoSec = imgSec.getWidth();
		BufferedImage resp = new BufferedImage(anchoPrinc, altoPrinc, imgPrinc.getType());
		int limInfX = posX + anchoSec;
		int limInfY = posY + altoSec;
		for (int i = 0; i < altoPrinc; i++) {
			for (int j = 0; j < anchoPrinc; j++) {
				int pixel = imgPrinc.getRGB(j, i);
				if (i >= posY && i < limInfY && j >= posX && j < limInfX) {
					int pixel2 = imgSec.getRGB((j - posX), (i - posY));
					int alpha = (pixel2 >> 24) & 0xff;
					if (alpha != 0) {
						pixel = pixel2;
					}
				}
				resp.setRGB(j, i, pixel);
			}
		}
		return resp;
	}
	
	private void dimensiones() {
		IContainer container = IContainer.make();
		int result = container.open(videoInput, IContainer.Type.READ, null);
		if(result>=0) {
			for (int i = 0; i < container.getNumStreams(); i++) {
				IStream stream = container.getStream(i);
				IStreamCoder coder = stream.getStreamCoder();
				if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
					videoW = coder.getWidth();
					videoH = coder.getHeight();
				}
			}
		}
	}

	public void getAudioStream() {
		IContainer container = IContainer.make();
		int result;
		if(audioOutput.equalsIgnoreCase("")) {
			result = container.open(videoInput, IContainer.Type.READ, null);
		}
		else {
			result = container.open(audioOutput,IContainer.Type.READ, null);		
		}
		if (result >= 0) {
			int numStreams = container.getNumStreams();
			int audiostreamt = -1;
			
			for (int i = 0; i < numStreams; i++) {
				IStream stream = container.getStream(i);
				IStreamCoder coder = stream.getStreamCoder();
				
				if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
					audiostreamt = i;
					break;
				}
			}
			
			if (audiostreamt != -1) {
				IStreamCoder audioCoder = container.getStream(audiostreamt).getStreamCoder();
				if (audioCoder.open() < 0) { throw new RuntimeException("Cant open audio coder"); }
				writer.addAudioStream(1, 1, audioCoder.getChannels(), audioCoder.getSampleRate());
				
				IPacket packetaudio = IPacket.make();
				
				while (container.readNextPacket(packetaudio) >= 0) {
					
					if (packetaudio.getStreamIndex() == audiostreamt) {
						IAudioSamples samples = IAudioSamples.make(512, audioCoder.getChannels(), IAudioSamples.Format.FMT_S32);
						int offset = 0;
						while (offset < packetaudio.getSize()) {
							int bytesDecodedaudio = audioCoder.decodeAudio(samples, packetaudio, offset);
							if (bytesDecodedaudio < 0) { throw new RuntimeException("could not detect audio"); }
							offset += bytesDecodedaudio;
							
							if (samples.isComplete()) {
								writer.encodeAudio(1, samples);
							}
						}
					}
				}
			}
		}
	}
}
