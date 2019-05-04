package sts.caster.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import sts.caster.util.TextureHelper;

public class GrowingVineEffect extends AbstractGameEffect
	{
	    private float x;
	    private float y;
	    private float sX;
	    private float sY;
	    private float tX;
	    private float tY;
	    private float scaleX;
	    private float scaleY;
	    private float targetScale;
	    private static Texture img;
	    private float initialDelay;
	    
	    public GrowingVineEffect(final float x, final float y, final float dX, final float dY, final float angle, final Color color1, float initialDelay) {
	        this(x, y, dX, dY, angle, 1.5f, color1, initialDelay);
	    }
	    
	    public GrowingVineEffect(final float x, final float y, final float dX, final float dY, final float angle, final float targetScale, final Color color1, float initialDelay) {
	        if (GrowingVineEffect.img == null) {
	            GrowingVineEffect.img = TextureHelper.getTexture("caster/images/vfx/vine.png");
	        }
	        this.x = x;
	        this.y = y;
	        this.sX = this.x;
	        this.sY = this.y;
	        this.tX = this.x + dX / 2.0f * Settings.scale;
	        this.tY = this.y + dY / 2.0f * Settings.scale;
	        this.color = color1.cpy();
	        this.color.a = 0.0f;
	        this.startingDuration = 1.5f;
	        this.duration = this.startingDuration;
	        this.targetScale = targetScale;
	        this.scaleX = 0.01f;
	        this.scaleY = 0.01f;
	        this.rotation = angle;
	        this.initialDelay = initialDelay;
	    }
	    
	    @Override
	    public void update() {
	    	if (initialDelay > 0) {
	    		initialDelay -= Gdx.graphics.getDeltaTime();
	    		return;
	    	}
	        if (duration > this.startingDuration / 2f) {
	            color.a = Interpolation.exp10In.apply(.9f, 0.0f, (duration - startingDuration / 2f) / (startingDuration / 2f));
	            scaleX = Interpolation.exp10In.apply(targetScale/2f, 0.1f, (duration - startingDuration / 2f) / (startingDuration / 2f));
	            scaleY = Interpolation.exp10In.apply(targetScale, 0.1f, (duration - startingDuration / 2f) / (startingDuration / 2f));
//	            y = sY + GrowingVineEffect.img.getHeight()*scaleY * Settings.scale/2f;
	            x = sX - GrowingVineEffect.img.getHeight()*scaleX * Settings.scale/2f;
	        }
	        else {
	            this.color.a = Interpolation.pow5In.apply(0.0f, 1f, duration / (startingDuration / 2f));
	        }
	        this.duration -= Gdx.graphics.getDeltaTime();
	        if (this.duration < 0.0f) {
	            this.isDone = true;
	        }
	    }
	    
	    @Override
	    public void render(final SpriteBatch sb) {
	    	sb.setColor(color);
//	        sb.setBlendFunction(770, 1);
	        sb.draw(GrowingVineEffect.img, x, y, 0, 0, 60.0f, 60.0f, scaleX, scaleY, rotation, 0, 0, 60, 60, false, false);
//	        sb.setColor(this.color);
//	        sb.draw(GrowingVineEffect.img, this.x, this.y, 64.0f, 64.0f, 128.0f, 128.0f, this.scaleX * 0.7f * MathUtils.random(0.95f, 1.05f), this.scaleY * MathUtils.random(0.95f, 1.05f), this.rotation, 0, 0, 128, 128, false, false);
//	        sb.setBlendFunction(770, 771);
	    }
	    
	    @Override
	    public void dispose() {
	        GrowingVineEffect.img.dispose();
	    }
	}
