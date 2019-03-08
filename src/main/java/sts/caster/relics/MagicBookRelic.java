package sts.caster.relics;

import java.util.function.Predicate;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import sts.caster.cards.CasterCard;
import sts.caster.cards.CasterCardTags;
import sts.caster.cards.spells.Meteor;
import sts.caster.core.CasterMod;
import sts.caster.patches.relics.MagicBookMemorizedCardField;
import sts.caster.util.TextureLoader;

public class MagicBookRelic extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {
    // This file will show you how to use 2 things - (Mostly) The Custom Bottle Relic and the Custom Savable - they go hand in hand.

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Savable
     *
     * Choose a card. Whenever you take play any card, draw the chosen card.
     */

    // BasemodWiki Says: "When you need to store a value on a card or relic between runs that isn't a relic's counter value
    // or a card's misc value, you use a custom savable to save and load it between runs."

    private static AbstractCard currentlyMemorizedCard;  // The field value we wish to save in this case is the card that's going to be in our bottle.
    private boolean cardSelected = true; // A boolean to indicate whether or not we selected a card for bottling.
    // (It's set to false on Equip)
    private boolean isStartOfgame = true;


    // ID, images, text.
    public static final String ID = CasterMod.makeID("MagicBook");
    private static final Texture IMG = TextureLoader.getTexture(CasterMod.makeRelicPath("BottledPlaceholder.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(CasterMod.makeRelicOutlinePath("BottledPlaceholder.png"));

    public MagicBookRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
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
        }
    }


    @Override
    public void onEquip() { // 1. When we acquire the relic
    	cardSelected = false; // 2. Tell the relic that we haven't bottled the card yet
    	if (!isStartOfgame) {
	        if (AbstractDungeon.isScreenUp) { // 3. If the map is open - hide it.
	            AbstractDungeon.dynamicBanner.hide();
	            AbstractDungeon.overlayMenu.cancelButton.hide();
	            AbstractDungeon.previousScreen = AbstractDungeon.screen;
	        }
	        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
	        // 4. Set the room to INCOMPLETE - don't allow us to use the map, etc.
	        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck); // 5. Get a card group of all currently unbottled cards
	        for (AbstractCard card : group.group) {
	        	if (!(card instanceof CasterCard) || !card.hasTag(CasterCardTags.SPELL) || ((CasterCard)card).baseDelayTurns < 1) {
	        		group.removeCard(card);
	        	}
	        }
	        AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[3] + name + DESCRIPTIONS[4], false, false, false, false);
	        // 6. Open the grid selection screen with the cards from the CardGroup we specified above. The description reads "Select a card to bottle for" + (relic name) + "."
    	}
    }

    @Override
    public void onTrigger() {
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
		MagicBookMemorizedCardField.inMagicBookField.set(cardToMemo, true); // Use our custom spire field to set that card to be bottled.
		if (cardToMemo instanceof CasterCard) {
			CasterCard casterCard = ((CasterCard)cardToMemo);
			casterCard.baseDelayTurns -= 1;
			casterCard.delayTurns -= 1;
			casterCard.applyPowers();
		}
		setDescriptionAfterLoading();
	}

	@Override
    public void onUnequip() { // 1. On unequip
        if (currentlyMemorizedCard != null) { // If the bottled card exists (prevents the game from crashing if we removed the bottled card from our deck for example.)
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(currentlyMemorizedCard); // 2. Get the card
            if (cardInDeck != null) {
                MagicBookMemorizedCardField.inMagicBookField.set(cardInDeck, false); // In our SpireField - set the card to no longer be bottled. (Unbottle it)
                if (cardInDeck instanceof CasterCard) {
                	CasterCard casterCard = ((CasterCard)cardInDeck);
                	casterCard.baseDelayTurns += 1;
                	casterCard.applyPowers();
                }
            }
        }
    }

    @Override
    public void update() {
        super.update(); //Do all of the original update() method in AbstractRelic

        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            // If the card hasn't been bottled yet and we have cards selected in the gridSelectScreen (from onEquip)
            cardSelected = true; //Set the cardSelected boolean to be true - we're about to bottle the card.
            currentlyMemorizedCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0); // The custom Savable "card" is going to equal
            // The card from the selection screen (it's only 1, so it's at index 0)
            applyMagicBookBuffToCard(currentlyMemorizedCard);
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE; // The room phase can now be set to complete (From INCOMPLETE in onEquip)
            AbstractDungeon.gridSelectScreen.selectedCards.clear(); // Always clear your grid screen after using it.
            setDescriptionAfterLoading(); // Set the description to reflect the bottled card (the method is at the bottom of this file)
        }
    }


    // Change description after relic is already loaded to reflect the bottled card.
    public void setDescriptionAfterLoading() {
        this.description = DESCRIPTIONS[1] + FontHelper.colorString(currentlyMemorizedCard.name, "y") + DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    // Standard description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
