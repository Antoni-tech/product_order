import { Box, useStyleConfig } from "@chakra-ui/react";

/**
 * Компонент карточка, представляет обычный прямоугольник
 * в котором распологаются другие компоненты
 * @param props 
 * @returns 
 */
export const Card = (props: any) => {
  const { variant, children, ...rest } = props;
  const styles = useStyleConfig("Card", { variant });
  // Pass the computed styles into the `__css` prop
  return (
    <Box __css={styles} {...rest}>
      {children}
    </Box>
  );
}
