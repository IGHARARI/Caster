package sts.caster.patches.spellCardType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import basemod.abstracts.CustomCard;
import basemod.helpers.SuperclassFinder;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.caster.cards.CasterCard;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class SpellCardTypeAbstractCardPatches {
    private static void renderHelper(final AbstractCard card, final SpriteBatch sb, final Color color, final AtlasRegion texture, final float xPos, final float yPos) {
        try {
            final Method renderHelperMethod = SuperclassFinder.getSuperClassMethod(card.getClass(), "renderHelper", SpriteBatch.class, Color.class, AtlasRegion.class, Float.TYPE, Float.TYPE);
            renderHelperMethod.setAccessible(true);
            final Field renderColorField = SuperclassFinder.getSuperclassField(card.getClass(), "renderColor");
            renderColorField.setAccessible(true);
            final Color renderColor = ((Color)renderColorField.get(card)).cpy();
            renderHelperMethod.invoke(card, sb, renderColor, texture, xPos, yPos);
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | SecurityException ex2) {
            ex2.printStackTrace();
        }
    }
	

	@SpirePatch(clz=AbstractCard.class, method="getCardBg", paramtypez = {})
	public static class getBgPatch {

		public static SpireReturn<Texture> Prefix(AbstractCard __card) {
			if (__card.type == CasterCardType.SPELL) {
				return SpireReturn.Return(ImageMaster.CARD_SKILL_BG_SILHOUETTE.getTexture());
			}
			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz=AbstractCard.class, method="getCardBgAtlas", paramtypez = {})
	public static class getCardBgAtlasPatch {

		public static SpireReturn<TextureAtlas.AtlasRegion> Prefix(AbstractCard __card) {
			if (__card.type == CasterCardType.SPELL) {
				return SpireReturn.Return(ImageMaster.CARD_SKILL_BG_SILHOUETTE);
			}
			return SpireReturn.Continue();
		}
	}
	
	
	@SpirePatch(clz=AbstractCard.class, method="renderCardBg", paramtypez = {SpriteBatch.class, float.class, float.class})
	public static class renderCardBackgroundPatch {

		public static SpireReturn<?> Prefix(AbstractCard __card, SpriteBatch __sb, float __xpos, float __ypos) {
            if (!(__card instanceof CustomCard) ||
					__card.color == AbstractCard.CardColor.RED ||
					__card.color == AbstractCard.CardColor.GREEN ||
					__card.color == AbstractCard.CardColor.BLUE ||
					__card.color == AbstractCard.CardColor.COLORLESS ||
					__card.color == AbstractCard.CardColor.CURSE) {
                return (SpireReturn<?>)SpireReturn.Continue();
            }
            final CustomCard card = (CustomCard)__card;
            Texture texture = null;
            if ((card instanceof CasterCard) && card.type == CasterCardType.SPELL) {
            	CasterCard casterCard = (CasterCard) card;
            	String path = "caster/images/card_backgrounds/skill/";
            	switch (casterCard.cardElement) {
	        		case EARTH:
	        			casterCard.setBackgroundTexture(path + "earth_bg_s.png", path + "earth_bg_b.png");
	        			break;
	        		case FIRE:
	        			casterCard.setBackgroundTexture(path + "fire_bg_s.png", path + "fire_bg_b.png");
	        			break;
	        		case ICE:
	        			casterCard.setBackgroundTexture(path + "ice_bg_s.png", path + "ice_bg_b.png");
	        			break;
	        		case THUNDER:
	        			casterCard.setBackgroundTexture(path + "lightning_bg_s.png", path + "lightning_bg_b.png");
	        			break;
	        		case DARK:
	        			break;
	        		case LIGHT:
	        			break;
	        		case NEUTRAL:
	        			break;
	        		default:
	        			break;
            	}
                texture = casterCard.getBackgroundSmallTexture();
            }
			if (card.textureBackgroundSmallImg != null && !card.textureBackgroundSmallImg.isEmpty()) {
                texture = card.getBackgroundSmallTexture();
            }
            if (texture == null) {
                return (SpireReturn<?>)SpireReturn.Continue();
            }
			AtlasRegion region = new AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
			renderHelper(card, __sb, Color.WHITE.cpy(), region, __xpos, __ypos);
			return (SpireReturn<?>)SpireReturn.Return((Object)null);
		}
	}
	
	
	@SpirePatch(clz=AbstractCard.class, method="renderPortraitFrame")
	public static class renderPortraitFramePatch {
		
		@SpireInsertPatch(locator=Locator.class, localvars={"tWidth", "tOffset"})
		public static void Insert(AbstractCard __instance, SpriteBatch sb, float x, float y, @ByRef float[]tOffset, @ByRef float[]tWidth) 
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
			if (__instance.type == CasterCardType.SPELL) {
				Method renderPortraitMethod = AbstractCard.class.getDeclaredMethod("renderSkillPortrait", SpriteBatch.class , float.class, float.class);
				renderPortraitMethod.setAccessible(true);
				renderPortraitMethod.invoke(__instance, sb, x, y);
				
                tWidth[0] = AbstractCard.typeWidthSkill;
                tOffset[0] = AbstractCard.typeOffsetSkill;
			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "renderDynamicFrame");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
	@SpirePatch(clz=AbstractCard.class, method="renderType")
	public static class renderTypePlate {
		
		@SpireInsertPatch(locator=Locator.class, localvars={"text"})
		public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef String[] text) {
			if (__instance.type == CasterCardType.SPELL) {
				text[0] = "Spell";
			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(com.megacrit.cardcrawl.helpers.FontHelper.class, "renderRotatedText");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
	@SpirePatch(clz=SingleCardViewPopup.class, method="renderCardTypeText")
	public static class renderTypePlateSingleCard {
		
		@SpireInsertPatch(locator=Locator.class, localvars={"label"})
		public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, @ByRef String[] label) 
				throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			Field __thisCardField = __instance.getClass().getDeclaredField("card");
			__thisCardField.setAccessible(true);
			AbstractCard __card = (AbstractCard) __thisCardField.get(__instance);
			if (__card != null && __card.type == CasterCardType.SPELL) {
				label[0] = "Spell";
			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(com.megacrit.cardcrawl.helpers.FontHelper.class, "renderFontCentered");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
	@SpirePatch(clz=SingleCardViewPopup.class, method="renderFrame")
	public static class renderFramePatch {
		
		@SpireInsertPatch(locator=Locator.class, localvars={"tmpImg", "tWidth", "tOffset"})
		public static void Insert(SingleCardViewPopup __instance, SpriteBatch _sb, @ByRef Texture[] tmpImg, @ByRef float[]tOffset, @ByRef float[]tWidth) 
				throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			Field __thisCardField = __instance.getClass().getDeclaredField("card");
			__thisCardField.setAccessible(true);
			AbstractCard __card = (AbstractCard) __thisCardField.get(__instance);
			if (__card != null && __card.type == CasterCardType.SPELL) {
                tWidth[0] = AbstractCard.typeWidthSkill;
                tOffset[0] = AbstractCard.typeOffsetSkill;
                switch (__card.rarity) {
                    case COMMON: {
                        tmpImg[0] = ImageMaster.CARD_FRAME_SKILL_COMMON_L.getTexture();
                        break;
                    }
                    case UNCOMMON: {
                        tmpImg[0] = ImageMaster.CARD_FRAME_SKILL_UNCOMMON_L.getTexture();
                        break;
                    }
                    case RARE: {
                        tmpImg[0] = ImageMaster.CARD_FRAME_SKILL_RARE_L.getTexture();
                        break;
                    }
                    default: {
                        tmpImg[0] = ImageMaster.CARD_FRAME_SKILL_COMMON_L.getTexture();
                    }
                }
			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
	@SpirePatch(clz=SingleCardViewPopup.class, method="renderCardBack", paramtypez={SpriteBatch.class})
	public static class renderSingleCardBackgroundPatch {
		
		public static void Prefix(SingleCardViewPopup __instance, SpriteBatch _sb) 
				throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			Field __thisCardField = __instance.getClass().getDeclaredField("card");
			__thisCardField.setAccessible(true);
			AbstractCard __card = (AbstractCard) __thisCardField.get(__instance);
            if (__card != null && (__card instanceof CasterCard) && __card.type == CasterCardType.SPELL) {
            	CasterCard casterCard = (CasterCard) __card;
            	String path = "caster/images/card_backgrounds/skill/";
            	switch (casterCard.cardElement) {
	        		case EARTH:
	        			casterCard.setBackgroundTexture(path + "earth_bg_s.png", path + "earth_bg_b.png");
	        			break;
	        		case FIRE:
	        			casterCard.setBackgroundTexture(path + "fire_bg_s.png", path + "fire_bg_b.png");
	        			break;
	        		case ICE:
	        			casterCard.setBackgroundTexture(path + "ice_bg_s.png", path + "ice_bg_b.png");
	        			break;
	        		case THUNDER:
	        			casterCard.setBackgroundTexture(path + "lightning_bg_s.png", path + "lightning_bg_b.png");
	        			break;
	        		case DARK:
	        			break;
	        		case LIGHT:
	        			break;
	        		case NEUTRAL:
	        			break;
	        		default:
	        			break;
            	}
            	if (casterCard.textureBackgroundLargeImg != null && !casterCard.textureBackgroundLargeImg.isEmpty()) {
            		_sb.draw(casterCard.getBackgroundLargeTexture(), Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
            	}
            }
			
		}
	}
	
}
