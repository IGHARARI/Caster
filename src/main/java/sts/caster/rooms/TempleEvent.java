package sts.caster.rooms;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Vajra;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import sts.caster.cards.CasterCard;
import sts.caster.cards.spells.Fira;
import sts.caster.cards.spells.Heavy;
import sts.caster.cards.spells.Slick;
import sts.caster.cards.spells.Thundara;
import sts.caster.core.CasterMod;

public class TempleEvent extends AbstractImageEvent {
    public static final String ID = CasterMod.makeID("TheTemple");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static final String DIALOG_1;
    int currentScreen;
	private boolean selectingCard;
    
    public TempleEvent() {
    	super(NAME, DIALOG_1, null);
		imageEventText.setDialogOption(TempleEvent.OPTIONS[0]);
		imageEventText.setDialogOption(TempleEvent.OPTIONS[1]);
		currentScreen = 1;
    }
    
    protected void buttonEffect(final int buttonPressed) {
    	switch (currentScreen) {
			case 1:
				processFirstScreenButtonPress(buttonPressed);
				break;
			case 2:
				processSecondScreenButtonPress(buttonPressed);
				break;
			case 3:
				processThirdScreenButtonPress(buttonPressed);
				break;
	
			default:
				break;
		}
    }

    private void moveToPageTwo() {
    	this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
    	this.imageEventText.clearAllDialogs();
    	imageEventText.setDialogOption(TempleEvent.OPTIONS[2], new Heavy());
    	imageEventText.setDialogOption(TempleEvent.OPTIONS[3], new Fira());
    	imageEventText.setDialogOption(TempleEvent.OPTIONS[4], new Thundara());
    	imageEventText.setDialogOption(TempleEvent.OPTIONS[5], new Slick());
		imageEventText.setDialogOption(TempleEvent.OPTIONS[6]);
    	currentScreen = 2;
    }
    
    private void moveToPageThree() {
    	this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
    	this.imageEventText.clearAllDialogs();
    	imageEventText.setDialogOption(TempleEvent.OPTIONS[7]);
    	currentScreen = 3;
    }
    
    private void processFirstScreenButtonPress(int buttonPressed) {
    	switch (buttonPressed) {
			case 0:
				moveToPageTwo();
				break;
			case 1:
                AbstractRelic relicToAdd = new Vajra();
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relicToAdd);
                moveToPageThree();
				break;
			default:
				break;
		}

    }

	private void processSecondScreenButtonPress(int buttonPressed) {
    	CasterCard c = null;
    	switch (buttonPressed) {
			case 0:
				c = new Heavy();
	            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
				break;
			case 1:
				c = new Fira();
				AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
				break;
			case 2:
				c = new Thundara();
				AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
				break;
			case 3:
				c = new Slick();
				AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
				break;
			case 4:
				if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
					AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[6], false, false, false, true);
					this.selectingCard = true;
				}
				return;
			default:
				break;
		}
    	
        moveToPageThree();
	}

	public void update() {
		super.update();
		if (this.selectingCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			AbstractCard offeredCard = AbstractDungeon.gridSelectScreen.selectedCards.remove(0);

			CardCrawlGame.sound.play("CARD_EXHAUST");
			AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(offeredCard, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
			AbstractDungeon.player.masterDeck.removeCard(offeredCard);
			this.selectingCard = false;
			moveToPageThree();
		}
	}

	private void processThirdScreenButtonPress(int buttonPressed) {
		openMap();
	}
	

	static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = TempleEvent.eventStrings.NAME;
        DESCRIPTIONS = TempleEvent.eventStrings.DESCRIPTIONS;
        OPTIONS = TempleEvent.eventStrings.OPTIONS;
        DIALOG_1 = TempleEvent.DESCRIPTIONS[0];
    }
    
}
