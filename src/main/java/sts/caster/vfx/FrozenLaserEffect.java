package sts.caster.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FrozenLaserEffect extends AbstractGameEffect {
    private float sX;
    private float sY;
    private float dX;
    private float dY;
    private float dst;
    private static final float DUR = 0.35f;
    private static TextureAtlas.AtlasRegion img;
    private boolean playedSfx;
    private Color secondaryColor;
    
    public FrozenLaserEffect(final float sX, final float sY, final float dX, final float dY) {
        if (FrozenLaserEffect.img == null) {
        	FrozenLaserEffect.img = ImageMaster.vfxAtlas.findRegion("combat/laserThin");
        }
        this.sX = sX;
        this.sY = sY;
        this.dX = dX;
        this.dY = dY;
        this.dst = Vector2.dst(this.sX, this.sY, this.dX, this.dY) / Settings.scale;
        this.color = Color.BLUE.cpy();// new Color(255, 210, 30, 1);
        this.secondaryColor = Color.WHITE.cpy();// new Color(255, 210, 30, 1);
        this.duration = DUR;
        this.startingDuration = DUR;
        this.rotation = MathUtils.atan2(dX - sX, dY - sY);
        this.rotation *= 57.295776f;
        this.rotation = -this.rotation + 90.0f;
        this.playedSfx = false; 
    }
    
    @Override
    public void update() {
        if (!this.playedSfx) {
            this.playedSfx = true;
			CardCrawlGame.sound.play("ATTACK_MAGIC_FAST_1");
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 2.0f) {
            this.color.a = Interpolation.pow2In.apply(1.0f, 0.0f, (this.duration - 0.25f) * 4.0f);
        }
        else {
            this.color.a = Interpolation.bounceIn.apply(0.0f, 1.0f, this.duration * 4.0f);
        }
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }
    
    @Override
    public void render(final SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(FrozenLaserEffect.img, this.sX, this.sY - FrozenLaserEffect.img.packedHeight / 2.0f + 10.0f * Settings.scale, 0.0f, FrozenLaserEffect.img.packedHeight / 2.0f, this.dst, 50.0f, this.scale + MathUtils.random(-0.01f, 0.01f), this.scale, this.rotation);
        secondaryColor.a = color.a;
        sb.setColor(secondaryColor);
        sb.draw(FrozenLaserEffect.img, this.sX, this.sY - FrozenLaserEffect.img.packedHeight / 2.0f, 0.0f, FrozenLaserEffect.img.packedHeight / 2.0f, this.dst, MathUtils.random(50.0f, 90.0f), this.scale + MathUtils.random(-0.02f, 0.02f), this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }
    
    @Override
    public void dispose() {
    }
}
