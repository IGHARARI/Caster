package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.util.BattleHelper;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class PackedIce extends CasterCard {

    public static final String ID = CasterMod.makeID("PackedIce");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_BLOCK = 7;
    private static final int UPG_BLOCK = 3;
    private static final int WEAK_AMOUNT = 1;
    private static final int BLOCK_THRESHOLD = 15;

    public PackedIce() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = block = BASE_BLOCK;
        baseMagicNumber = magicNumber = WEAK_AMOUNT;
        baseM2 = m2 = BLOCK_THRESHOLD;
        setCardElement(MagicElement.ICE);
    }

     @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
         addToBot(new GainBlockAction(p, block));
         addToBot(new AbstractGameAction() {
             @Override
             public void update() {
                 if (AbstractDungeon.player.currentBlock >= m2) {
                     ArrayList<AbstractMonster> mons = BattleHelper.getAliveMonsters();
                     for (AbstractMonster m : mons) {
                         addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false)));
                     }
                 }
                 isDone = true;
             }
         });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK);
            initializeDescription();
        }
    }
}
