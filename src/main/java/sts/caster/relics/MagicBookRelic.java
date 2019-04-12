package sts.caster.relics;

import java.util.function.Predicate;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import sts.caster.cards.CasterCard;
import sts.caster.cards.spells.Meteor;
import sts.caster.core.CasterMod;
import sts.caster.patches.relics.MagicBookMemorizedCardField;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.util.TextureHelper;

public class MagicBookRelic extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {

    private static AbstractCard currentlyMemorizedCard; 
    private boolean cardSelected = true; 
    private boolean isStartOfgame = true;


    // ID, images, text.
    public static final String ID = CasterMod.makeID("MagicBook");
    private static final Texture IMG = TextureHelper.getTexture(CasterMod.makeRelicPath("BottledPlaceholder.png"));
    private static final Texture OUTLINE = TextureHelper.getTexture(CasterMod.makeRelicOutlinePath("BottledPlaceholder.png"));

    public MagicBookRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    public String getMemorizedCardName() {
    	return currentlyMemorizedCard != null ? currentlyMemorizedCard.name : "...";
    }
    
    @Override
    public Predicate<AbstractCard> isOnCard() {
        return MagicBookMemorizedCardField.inMagicBookField::get;
    }

    @Override
    public Integer onSave() {
        if (currentlyMemorizedCard != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(currentlyMemorizedCard);
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            currentlyMemorizedCard = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (currentlyMemorizedCard != null) {
                applyMagicBookBuffToCard(currentlyMemorizedCard);
            }
            isStartOfgame = false;
        }
    }


    @Override
    public void onEquip() { 
    	System.out.println("on equip for magic book is start? " + isStartOfgame);
    	cardSelected = false; 
    	if (!isStartOfgame) {
	        if (AbstractDungeon.isScreenUp) { 
	            AbstractDungeon.dynamicBanner.hide();
	            AbstractDungeon.overlayMenu.cancelButton.hide();
	            AbstractDungeon.previousScreen = AbstractDungeon.screen;
	        }
	        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
	        CardGroup nonBottledCards = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
	        CardGroup nonBottledSpells = new CardGroup(CardGroupType.UNSPECIFIED);
	        for (AbstractCard card : nonBottledCards.group) {
	        	if (card.type == CasterCardType.SPELL && (card instanceof CasterCard) && ((CasterCard)card).baseDelayTurns > 0) {
	        		System.out.println("magbook: found spell - " + card.name);
	        		nonBottledSpells.addToBottom(card);
	        	}
	        }
	        AbstractDungeon.gridSelectScreen.open(nonBottledSpells, 1, DESCRIPTIONS[3] + name + DESCRIPTIONS[4], false, false, false, false);
    	}
    }

    @Override
    public void onTrigger() {
//    	isStartOfgame = false;
//    	onEquip();
    	if (!cardSelected && isStartOfgame) {
    		AbstractCard meteor = null;
    		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
    			if (c instanceof Meteor) meteor = c;
    		}
    		currentlyMemorizedCard = meteor;
    		System.out.println("is meteor null? " + (meteor == null));
    		if (currentlyMemorizedCard != null) {
    			cardSelected = true;
    			isStartOfgame = false;
    			applyMagicBookBuffToCard(currentlyMemorizedCard);
    		}
    	}
    }

    private void applyMagicBookBuffToCard(AbstractCard cardToMemo) {
		MagicBookMemorizedCardField.inMagicBookField.set(cardToMemo, true); 
//		if (cardToMemo instanceof CasterCard) {
//			CasterCard casterCard = ((CasterCard)cardToMemo);
//			casterCard.baseDelayTurns -= 1;
//			casterCard.delayTurns = casterCard.baseDelayTurns;
//		}
		setDescriptionAfterLoading();
	}

	@Override
    public void onUnequip() { 
        if (currentlyMemorizedCard != null) { 
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(currentlyMemorizedCard); 
            if (cardInDeck != null) {
                MagicBookMemorizedCardField.inMagicBookField.set(cardInDeck, false); 
//                if (cardInDeck instanceof CasterCard) {
//                	CasterCard casterCard = ((CasterCard)cardInDeck);
//                	casterCard.baseDelayTurns += 1;
//                	casterCard.delayTurns = casterCard.baseDelayTurns;
//                }
            }
        }
    }

	@Override
	public void atBattleStart() {
        for (final AbstractCard abstractCard : GetAllInBattleInstances.get(currentlyMemorizedCard.uuid)) {
    		if (abstractCard instanceof CasterCard) {
				CasterCard casterCard = ((CasterCard)abstractCard);
				casterCard.baseDelayTurns -= 1;
				casterCard.delayTurns = casterCard.baseDelayTurns;
    		}
        }
	}
	
    @Override
    public void update() {
        super.update(); 

        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true; 
            currentlyMemorizedCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0); 
            applyMagicBookBuffToCard(currentlyMemorizedCard);
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE; 
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading(); 
        }
    }


    public void setDescriptionAfterLoading() {
        this.description = DESCRIPTIONS[1] + FontHelper.colorString(currentlyMemorizedCard.name, "y") + DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
