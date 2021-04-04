package com.tol.researchstages.compat.patchouli.utils;

import net.minecraftforge.fml.ModList;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class BookReloadUtils {

    public static void patchouliUpdateConditional() {
        if (ModList.get().getModContainerById("patchouli").isPresent()) {
            for (Book book : BookRegistry.INSTANCE.books.values()) {
                if (book.getModNamespace().equals("patchouli")) {
                    book.reloadContentsAndExtensions();
                    book.reloadLocks(false);
                }
            }
        }
    }
}
