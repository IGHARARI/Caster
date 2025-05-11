package sts.caster.patches.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.cards.CasterCardTags;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RenderElectrifiedTagPatch {
	
	private static final Color fontColor = Settings.CREAM_COLOR.cpy();
	private static final Color baseColor = Color.WHITE.cpy();
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ElectrifiedStrings");
	
	@SpirePatch(clz=AbstractCard.class, method="render", paramtypez = {SpriteBatch.class})
	public static class cardRenderPatch {
		
		public static void Postfix(AbstractCard __card, SpriteBatch __sb) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	        if (!Settings.hideCards) {

	            if (AbstractDungeon.player != null) {
	                if (__card.hasTag(CasterCardTags.ELECTRIFIED)) {
	                	float drawX = __card.current_x;
	                	float drawY = __card.current_y;
	                	long elecAmount = __card.tags.stream().filter((tag) -> tag == CasterCardTags.ELECTRIFIED).count();
	                	Texture elecTabTexture = TextureHelper.getTexture(CasterMod.ELECTRIFIED_TAG_IMAGE);
						AtlasRegion elecTab = new AtlasRegion(elecTabTexture, 0, 0, elecTabTexture.getWidth(), elecTabTexture.getHeight());
        				Method renderHelperMethod = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, AtlasRegion.class, float.class, float.class);
	                	renderHelperMethod.setAccessible(true);
	                	renderHelperMethod.invoke(__card, __sb, baseColor, elecTab, drawX, drawY);
	                    BitmapFont font = FontHelper.menuBannerFont;
	                    font.getData().setScale(1.0F);
	                    String elecMessage = uiStrings.TEXT[0] + " x"+elecAmount;
	                    GlyphLayout gl = new GlyphLayout(font, elecMessage);
	                    float scale = Math.min((116.0F*__card.drawScale)/gl.width, (21.0F*__card.drawScale)/gl.height) * Settings.scale;
	                    FontHelper.menuBannerFont.getData().setScale(scale);
	                    FontHelper.renderRotatedText(__sb, FontHelper.menuBannerFont, elecMessage, __card.current_x, __card.current_y, 0.0F, 443.0F * Settings.scale * __card.drawScale / 2.0F, __card.angle, true, fontColor);
	                    FontHelper.menuBannerFont.getData().setScale(1.0F);
	                }
	            }
	        }
		}
	}
}
