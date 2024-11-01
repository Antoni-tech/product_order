import React, {useEffect} from 'react';
import {Dropdown} from "flowbite";
import type {DropdownOptions, DropdownInterface} from "flowbite";
import {Box} from '@chakra-ui/react';

interface MyCollapseProps {
    targetId: string;
    triggerId: string;
    children?: React.ReactNode;
    options?: DropdownOptions;
}

const MyCollapse: React.FC<MyCollapseProps> = ({
                                                   targetId,
                                                   triggerId,
                                                   options = {},
                                                   children,
                                               }) => {

    useEffect(() => {
        const targetEl: HTMLElement | null = document.getElementById(targetId);
        const triggerEl: HTMLElement | null = document.getElementById(triggerId);

        if (targetEl && triggerEl) {
            // Define the collapse options
            const collapseOptions: DropdownOptions = {
                placement: 'bottom',
                offsetSkidding: 0,
                offsetDistance: 27.5,
                triggerType: 'hover',

                onHide: () => {
                    console.log('dropdown has been hidden');
                },
                onShow: () => {
                    console.log('dropdown has been shown');
                },
                onToggle: () => {
                    console.log('element has been toggled');
                },
            };

            targetEl.style.width = '100%';
            const dropdown: DropdownInterface = new Dropdown(targetEl, triggerEl, collapseOptions);
        }
    }, [targetId, triggerId]);

    return <Box style={{zIndex: 25}}>{children}</Box>;
};

export default MyCollapse;
