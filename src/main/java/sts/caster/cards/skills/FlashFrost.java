package sts.caster.cards.skills;

import static sts.caster.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.CasterMod;
import sts.caster.cards.CasterCard;
import sts.caster.characters.TheCaster;
import sts.caster.powers.FrozenPower;

public class FlashFrost extends CasterCard {

    public static final String ID = CasterMod.makeID("FlashFrost");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int BASE_FROST = 3;
    private static final int UPGRADE_FROST = 2;


    public FlashFrost() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_FROST;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mon, p, new FrozenPower(mon, p, magicNumber), magicNumber));
    	}
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeMagicNumber(UPGRADE_FROST);;
        }
    }
}
