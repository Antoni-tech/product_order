import { Text } from "@chakra-ui/react";
import { FC } from "react";
import { FaRegFileExcel } from "react-icons/fa";
import { useDispatch } from "react-redux";
import { ModalService } from "../../service/Modal/ModalService";
import { CreateButton } from "./CreateButton";
import { Option } from "../../shared/utilities/OptionT";
var fileDownload = require('js-file-download');

/**
 * Кнопка скачивания документов в реестрах
 * 
 * Принимает функцию, которая должна вызываться по названию
 * @param param0 
 * @returns 
 */
export const DownloadButton: FC<{ clickAction: Function, height?: string }> = ({ clickAction, height }) => {
    const dispatch = useDispatch()
    const modalService = new ModalService(dispatch)

    const handleClick = () => {
        clickAction()?.then((res: Option<any>) => {
            console.log(res.Some)
            if (res?.None) {
                modalService.setModalData({
                    onSubmit: () => modalService.deleteModalData(),
                    isOpen: true,
                    onOpen: () => { },
                    onClose: () => modalService.deleteModalData(),
                    modalTextData: "Не удалось скачать файл",
                    context: "error"
                })
            } else {
                fileDownload(res.Some, 'report.xlsx');
            }
        })
    }

    return (
        <CreateButton h={height ? height : "50px"} onClick={handleClick}>
            <FaRegFileExcel />
            <Text ml={"5px"}>
                Скачать в Excel
            </Text>
        </CreateButton>
    )
}