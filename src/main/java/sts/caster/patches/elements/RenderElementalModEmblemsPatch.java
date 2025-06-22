package sts.caster.patches.elements;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import sts.caster.cards.CasterCardTags;
import sts.caster.cards.mods.IgnitedCardMod;
import sts.caster.util.TextureHelper;

import java.lang.reflect.InvocationTargetException;

import static sts.caster.core.CasterMod.makeVFXPath;

public class RenderElementalModEmblemsPatch {

	private static final Texture electrifiedEmblem = TextureHelper.getTexture(makeVFXPath("electrified_emblem.png"));
	private static final Texture ignitedEmblem = TextureHelper.getTexture(makeVFXPath("fire_emblem.png"));

	@SpirePatch(clz=AbstractCard.class, method="render", paramtypez = {SpriteBatch.class})
	public static class cardRenderPatch {
		
		public static void Postfix(AbstractCard __card, SpriteBatch __sb) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	        if (!Settings.hideCards) {

	            if (AbstractDungeon.player != null) {
	                if (__card.hasTag(CasterCardTags.ELECTRIFIED) || CardModifierManager.hasModifier(__card, IgnitedCardMod.ID)) {
						long elecAmount = __card.tags.stream().filter((tag) -> tag == CasterCardTags.ELECTRIFIED).count();
						IgnitedCardMod ignitedMod = (IgnitedCardMod) CardModifierManager.getModifiers(__card, IgnitedCardMod.ID).stream().findFirst().orElse(null);

						BitmapFont font = FontHelper.menuBannerFont;
						font.getData().setScale(0.6F);
						if (ignitedMod != null) {
							ExtraIcons.renderIcon(__card, ignitedEmblem, 0, 0, Color.WHITE.cpy(), String.valueOf(ignitedMod.getIgnitedAmount()), font, 8f, -6f, Color.BLACK.cpy());
						}
						if (elecAmount > 0) {
							ExtraIcons.renderIcon(__card, electrifiedEmblem, 0, 0, Color.WHITE.cpy(), String.valueOf(elecAmount), font, 8f, -6f, Color.BLACK.cpy());
						}
	                }
	            }
	        }
		}
	}
}
