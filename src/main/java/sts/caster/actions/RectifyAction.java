package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import basemod.BaseMod;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import sts.caster.cards.CasterCard;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.patches.spellCardType.CasterCardType;

public class RectifyAction extends AbstractGameAction implements basemod.helpers.ModalChoice.Callback {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RectifyAction");
    
	private boolean getForFree;
	
    public RectifyAction(boolean getForFree) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.getForFree = getForFree;
    }
    
    @Override
    public void update() {
        if (!this.isDone) {
        	this.isDone = true;
        	ModalChoice modal = new ModalChoiceBuilder()
                    .setCallback(this)
                    .setTitle(uiStrings.TEXT[0])
                    .setColor(TheCaster.Enums.THE_CASTER_COLOR)
                    .addOption(uiStrings.TEXT[1], uiStrings.TEXT[2], CardTarget.NONE)
                    .addOption(uiStrings.TEXT[3], uiStrings.TEXT[4], CardTarget.NONE)
                    .addOption(uiStrings.TEXT[5], uiStrings.TEXT[6], CardTarget.NONE)
                    .addOption(uiStrings.TEXT[7], uiStrings.TEXT[8], CardTarget.NONE)
                    .create();
        	modal.open();
        }
    }

	@Override
	public void optionSelected(AbstractPlayer player, AbstractMonster mon, int optionChosen) {
        switch (optionChosen) {
	        case 0:
	        	showAndObtainCard(generateRandomSpell(MagicElement.FIRE), getForFree);
	            break;
	        case 1:
	        	showAndObtainCard(generateRandomSpell(MagicElement.THUNDER), getForFree);
	            break;
	        case 2:
	        	showAndObtainCard(generateRandomSpell(MagicElement.ICE), getForFree);
	            break;
	        case 3:
	        	showAndObtainCard(generateRandomSpell(MagicElement.EARTH), getForFree);
	        	break;
	        default:
	            return;
	    }
		
	}

	private void showAndObtainCard(AbstractCard card, boolean getForFree) {
        final AbstractCard generatedCard = card.makeStatEquivalentCopy();
        generatedCard.current_x = -1000.0f * Settings.scale;
        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(generatedCard, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        }
        else {
            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(generatedCard, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        }
        if (getForFree) {
        	generatedCard.setCostForTurn(0);
        }
	}

	private AbstractCard generateRandomSpell(MagicElement element) {
        final ArrayList<AbstractCard> list = new ArrayList<AbstractCard>();
        for (final AbstractCard c : AbstractDungeon.srcCommonCardPool.group) {
            if (c.type == CasterCardType.SPELL && (c instanceof CasterCard) && ((CasterCard)c).cardElement == element) {
                list.add(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.srcUncommonCardPool.group) {
            if (c.type == CasterCardType.SPELL && (c instanceof CasterCard) && ((CasterCard)c).cardElement == element) {
                list.add(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.srcRareCardPool.group) {
            if (c.type == CasterCardType.SPELL && (c instanceof CasterCard) && ((CasterCard)c).cardElement == element) {
                list.add(c);
            }
        }
        return list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
	}
}
