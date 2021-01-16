package Spansion.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Shortcuts {
    public static boolean IntendsToAttack(AbstractMonster m) {
        return (m.intent == AbstractMonster.Intent.ATTACK |
                m.intent == AbstractMonster.Intent.ATTACK_BUFF |
                m.intent == AbstractMonster.Intent.ATTACK_DEBUFF |
                m.intent == AbstractMonster.Intent.ATTACK_DEFEND);
    }

    public static AbstractRelic.RelicTier GetRelicTier(AbstractRelic relic) {
        AbstractRelic.RelicTier ret = AbstractRelic.RelicTier.STARTER;
        if (AbstractDungeon.commonRelicPool.contains(relic)) {
            ret = AbstractRelic.RelicTier.COMMON;
        } else if (AbstractDungeon.uncommonRelicPool.contains(relic)) {
            ret = AbstractRelic.RelicTier.UNCOMMON;
        } else if (AbstractDungeon.rareRelicPool.contains(relic)) {
            ret = AbstractRelic.RelicTier.RARE;
        } else if (AbstractDungeon.bossRelicPool.contains(relic)) {
            ret = AbstractRelic.RelicTier.BOSS;
        } else if (AbstractDungeon.shopRelicPool.contains(relic)) {
            ret = AbstractRelic.RelicTier.SHOP;
        }
        return ret;
    }
}

