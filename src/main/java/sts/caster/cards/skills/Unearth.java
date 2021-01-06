package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.List;
import java.util.function.Consumer;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

public class Unearth extends CasterCard {

	public static final String ID = CasterMod.makeID("Unearth");
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String IMG = makeCardPath("unearth.png");

	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;
	private static final CardType TYPE = CardType.SKILL;
	public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

	private static final int COST = 0;
	private static final int FETCH_AMT = 1;
	private static final int HEAL_MULT = 1;

	private static final Consumer<List<AbstractCard>> buildFetchConsumer(int multiplier) {
		return (cardsRetrieved) -> {
			int energySum = 0;
			for (AbstractCard c : cardsRetrieved) {
				if (c.costForTurn > 0) energySum += c.costForTurn;
			}
			if (energySum > 0) AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, energySum*multiplier));
		};
	}
	
	public Unearth() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		magicNumber = baseMagicNumber = FETCH_AMT;
		m2 = baseM2 = HEAL_MULT;
		setCardElement(MagicElement.EARTH);
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new FetchAction(p.drawPile, isCardSpellPredicate, magicNumber, buildFetchConsumer(m2)));
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
			upgradeM2(HEAL_MULT);
		}
	}
}
