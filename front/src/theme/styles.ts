import {ChakraTheme} from "@chakra-ui/react";
import { mode } from "@chakra-ui/theme-tools";

export const globalStyles:Partial<ChakraTheme>= {
  colors: {
    gray: {
      700: "#1f2733",
    },
  },
  fonts: {
    fontFamily: 'Oswald, sans-serif'
  },
  styles: {
      global: (props:any) => ({
      body: {
        bg: mode("gray.50", "gray.800")(props),
        fontFamily: 'Oswald, sans-serif'
      },
      html: {
        fontFamily: 'Oswald, sans-serif'
      }
    }),
  },
};
