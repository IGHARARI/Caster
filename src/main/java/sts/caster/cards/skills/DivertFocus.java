package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.FocusPower;

import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.powers.GainFocusPower;

public class DivertFocus extends CasterCard {

    public static final String ID = CasterMod.makeID("DivertFocus");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("divertfocus.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_BLOCK = 9;
    private static final int UPG_BLOCK = 3;
    private static final int BASE_MODIFY_AMOUNT = 1;


    public DivertFocus() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BASE_BLOCK;
        magicNumber = baseMagicNumber = BASE_MODIFY_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new GainBlockAction(p, p, block));
		addToBot(new ApplyPowerAction(p, p, new FocusPower(p, -magicNumber), -magicNumber));
		if (!p.hasPower(ArtifactPower.POWER_ID)) {
			addToBot(new ApplyPowerAction(p, p, new GainFocusPower(p, magicNumber, true), magicNumber, true, AttackEffect.NONE));
		}
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeBlock(UPG_BLOCK);
        }
    }
}
