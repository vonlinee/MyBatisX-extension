package com.baomidou.mybatisx.plugin.provider;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.GutterName;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.codeInsight.daemon.MergeableLineMarkerInfo;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.Function;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @see com.intellij.execution.lineMarker.RunLineMarkerProvider
 */
public class MapperRunLineMarker extends LineMarkerProviderDescriptor {

    @Override
    public @Nullable("null means disabled") @GutterName String getName() {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        for (PsiElement element : elements) {

        }
    }

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }

    static class LineMarkInfo extends MergeableLineMarkerInfo<PsiElement> {

        public LineMarkInfo(@NotNull PsiElement element, @NotNull TextRange textRange, @NotNull Icon icon, @Nullable Function<? super PsiElement, String> tooltipProvider, @Nullable GutterIconNavigationHandler<PsiElement> navHandler, @NotNull GutterIconRenderer.Alignment alignment, @NotNull Supplier<@NotNull @Nls String> accessibleNameProvider) {
            super(element, textRange, icon, tooltipProvider, navHandler, alignment, accessibleNameProvider);
        }

        @Override
        public boolean canMergeWith(@NotNull MergeableLineMarkerInfo<?> info) {
            return false;
        }

        @Override
        public Icon getCommonIcon(@NotNull List<? extends MergeableLineMarkerInfo<?>> infos) {
            return null;
        }
    }
}
