package com.quange.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class EmojiTextView extends TextView {
	private Context context;
	
	public EmojiTextView(Context context) {
		super(context);
		this.context = context;
	}
	
	public EmojiTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public EmojiTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	public class StickerSpan extends ImageSpan {  
		  
	    public StickerSpan(Drawable b, int verticalAlignment) {  
	        super(b, verticalAlignment);  
	  
	    }  
	  
	    @Override  
	    public void draw(Canvas canvas, CharSequence text,  
	                     int start, int end, float x,  
	                     int top, int y, int bottom, Paint paint) {  
	        Drawable b = getDrawable();  
	        canvas.save();  
	        int transY = bottom - b.getBounds().bottom - 0;  
	        if (mVerticalAlignment == ALIGN_BASELINE) {  
	            int textLength = text.length();  
	            for (int i = 0; i < textLength; i++) {  
	                if (Character.isLetterOrDigit(text.charAt(i))) {  
	                    transY -= paint.getFontMetricsInt().descent;  
	                    break;  
	                }  
	            }  
	        }  
	        canvas.translate(x, transY);  
	        b.draw(canvas);  
	        canvas.restore();  
	    }  
	}  
	
	public void setEmojiText(String text) {
		text = EmojiUtils.convertTag(text);
		Spanned spanned = Html.fromHtml(text, emojiGetter, null);
		
		if (spanned instanceof SpannableStringBuilder) {  
	           ImageSpan[] imageSpans = spanned.getSpans(0, spanned.length(), ImageSpan.class);  
	           for (ImageSpan imageSpan : imageSpans) {  
	               int start = spanned.getSpanStart(imageSpan);  
	               int end = spanned.getSpanEnd(imageSpan);  
	               Drawable d = imageSpan.getDrawable();  
	               StickerSpan newImageSpan = new StickerSpan(d, ImageSpan.ALIGN_BASELINE);  
	               ((SpannableStringBuilder) spanned).setSpan(newImageSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);  
	               ((SpannableStringBuilder) spanned).removeSpan(imageSpan);  
	           }  
	    }  
		
		setText(spanned);
	}
	
	private ImageGetter emojiGetter = new ImageGetter()
	{
        public Drawable getDrawable(String source){
            // �摜�̃��\�[�XID���擾
            int id = getResources().getIdentifier(source, "drawable", context.getPackageName());
            
            Drawable emoji = getResources().getDrawable(id);
            int w = (int)(emoji.getIntrinsicWidth() / 3.0);
            int h = (int)(emoji.getIntrinsicHeight() /3.0);
            emoji.setBounds(0, 0, w, h);
            return emoji;
        }
    };		
}