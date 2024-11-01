import {ComponentStyleConfig} from "@chakra-ui/react";

const PanelContainer:ComponentStyleConfig = {
  baseStyle: {
    p: "30px 15px",
    minHeight: "calc(100vh - 123px)",
  },
};

export const PanelContainerComponent:any = {
  components: {
    PanelContainer,
  },
};
