package org.mifos.bi.test;

import org.junit.Test

class FundsMovementTest {

    def detailsHeader = ['Account ID', 'Client name', 'Loan Disbursal Date', 'Loan Amount Disbursed', 'No. of Installments',
            'Installment Frequency', 'Principal Outstanding', 'Interest Outstanding', 'Loan Fees Outstanding', 'Arrears amount', 'Arrears days']

    def firstFund = ['Source of Fund:', '-']
    def detailsFirstFundFirstAccount_20101231 = ['32', 'br2 client 1 xxx', '2010-07-21', '3000.0000', '7', '1 week(s)', '2924.9000', '18.1000', '0.0000', '0.0000', '0']
    def detailsFirstFundSecondAccount_20100723 = ['38', 'lose group ..', '2010-07-22', '3000.0000', '7', '1 week(s)', '3000.0000', '2.9000', '0.0000', '0.0000', '0']
    def detailsFirstFundSecondAccount_20101231 = ['38', 'lose group ..', '2010-07-22', '3000.0000', '7', '1 week(s)', '0.0000', '0.0000', '0.0000', '0.0000', '0']
    def detailsFirstFundThirdAccount_20101231 = ['44', 'xfer to gr in ..', '2010-07-22', '6000.0000', '9', '1 week(s)', '6000.0000', '5.8000', '0.0000', '0.0000', '0']
    def subTotalFirstFund_20100723 = ['sub-total', '3', ' ', '12000.0000', ' ', ' ', '11924.9000', '26.8000', '0.0000', '0.0000', ' ']
    def subTotalFirstFund_20101231 = ['sub-total', '3', ' ', '12000.0000', ' ', ' ', '8924.9000', '23.9000', '0.0000', '0.0000', ' ']
    def subTotalFirstFundBO1Branch_20101231 = ['sub-total', '2', ' ', '9000.0000', ' ', ' ', '6000.0000', '5.8000', '0.0000', '0.0000', ' ']

    def secondFund = ['Source of Fund:', 'Funding Org A']
    def detailsSecondFundFirstAccount_20101231 = ['4', 'client dw1 1 1 ..', '2010-07-16', '3000.0000', '7', '1 week(s)', '2752.9000', '0.0000', '0.0000', '0.0000', '0']
    def subTotalSecondFund_20101231 = ['sub-total', '1', ' ', '3000.0000', ' ', ' ', '2752.9000', '0.0000', '0.0000', '0.0000', ' ']

    def pageFooter = ['Version 1.1', 'Page', '1 / 1']

    @Test
    void testParams_All_All_20101231() {
        new PrptReport().execute () { t ->

            // Report settings.
            t.reportPath = 'reports/standardReports/FundsMovement.prpt'
            t.reportParams = ['selected_office': '-1', 'selected_funder': '-1', 'out_date': '2010-12-31']

            // Report title
            t.assertRowEquals(1, ['Funds Movement Report'])

            // Page header
            t.assertCellEquals(2, 1, 'As Of:')
            t.assertCellEquals(2, 2, '2010-12-31')
            t.assertCellEquals(2, 3, 'Report Date:')
            t.assertRowEquals(3, ['Branch:', ' All'])
            t.assertRowEquals(4, ['Funding:', ' All'])

            // 1st Group header
            t.assertRowEquals(5, firstFund)
            // 1st Details header
            t.assertRowEquals(6, detailsHeader)
            // Fund: '-' -> Account ID: 32
            t.assertRowEquals(7, detailsFirstFundFirstAccount_20101231)
            // Fund: '-' -> Account ID: 38
            t.assertRowEquals(8, detailsFirstFundSecondAccount_20101231)
            // Fund: '-' -> Account ID: 44
            t.assertRowEquals(9, detailsFirstFundThirdAccount_20101231)
            // 1st Group footer
            t.assertRowEquals(10, subTotalFirstFund_20101231)

            // 2nd Group header
            t.assertRowEquals(11, secondFund)
            // 2nd Details header
            t.assertRowEquals(12, detailsHeader)
            // Fund: 'Funding Org A' -> Account ID: 4
            t.assertRowEquals(13, detailsSecondFundFirstAccount_20101231)
            // 2nd Group footer
            t.assertRowEquals(14, subTotalSecondFund_20101231)

            // Page footer
            t.assertRowEquals(15, pageFooter)
        }
    }

    @Test
    void testParams_BO1_All_20101231() {
        new PrptReport().execute () { t ->

            // Report settings.
            t.reportPath = 'reports/standardReports/FundsMovement.prpt'
            t.reportParams = ['selected_office': '1.1.3.2.', 'selected_funder': '-1', 'out_date': '2010-12-31']

            // Report title
            t.assertRowEquals(1, ['Funds Movement Report'])

            // Page header
            t.assertCellEquals(2, 1, 'As Of:')
            t.assertCellEquals(2, 2, '2010-12-31')
            t.assertCellEquals(2, 3, 'Report Date:')
            t.assertRowEquals(3, ['Branch:', 'BO 1'])
            t.assertRowEquals(4, ['Funding:', ' All'])

            // 1st Group header
            t.assertRowEquals(5, firstFund)
            // 1st Details header
            t.assertRowEquals(6, detailsHeader)
            // Fund: '-' -> Account ID: 38
            t.assertRowEquals(7, detailsFirstFundSecondAccount_20101231)
            // Fund: '-' -> Account ID: 44
            t.assertRowEquals(8, detailsFirstFundThirdAccount_20101231)
            // 1st Group footer
            t.assertRowEquals(9, subTotalFirstFundBO1Branch_20101231)

            // 2nd Group header
            t.assertRowEquals(10, secondFund)
            // 2nd Details header
            t.assertRowEquals(11, detailsHeader)
            // Fund: 'Funding Org A' -> Account ID: 4
            t.assertRowEquals(12, detailsSecondFundFirstAccount_20101231)
            // 2nd Group footer
            t.assertRowEquals(13, subTotalSecondFund_20101231)

            // Page footer
            t.assertRowEquals(14, pageFooter)
        }
    }

    @Test
    void testParams_BO1_OrgA_20101231() {
        new PrptReport().execute () { t ->

            // Report settings.
            t.reportPath = 'reports/standardReports/FundsMovement.prpt'
            t.reportParams = ['selected_office': '1.1.3.2.', 'selected_funder': '2', 'out_date': '2010-12-31']

            // Report title
            t.assertRowEquals(1, ['Funds Movement Report'])

            // Page header
            t.assertCellEquals(2, 1, 'As Of:')
            t.assertCellEquals(2, 2, '2010-12-31')
            t.assertCellEquals(2, 3, 'Report Date:')
            t.assertRowEquals(3, ['Branch:', 'BO 1'])
            t.assertRowEquals(4, ['Funding:', 'Funding Org A'])

            // 1st Group header
            t.assertRowEquals(5, secondFund)
            // 1st Details header
            t.assertRowEquals(6, detailsHeader)
            // Fund: 'Funding Org A' -> Account ID: 4
            t.assertRowEquals(7, detailsSecondFundFirstAccount_20101231)
            // 1st Group footer
            t.assertRowEquals(8, subTotalSecondFund_20101231)

            // Page footer
            t.assertRowEquals(9, pageFooter)
        }
    }

    @Test
    void testParams_All_All_20100723() {
        new PrptReport().execute () { t ->

            // Report settings.
            t.reportPath = 'reports/standardReports/FundsMovement.prpt'
            t.reportParams = ['selected_office': '-1', 'selected_funder': '-1', 'out_date': '2010-07-23']

            // Report title
            t.assertRowEquals(1, ['Funds Movement Report'])

            // Page header
            t.assertCellEquals(2, 1, 'As Of:')
            t.assertCellEquals(2, 2, '2010-07-23')
            t.assertCellEquals(2, 3, 'Report Date:')
            t.assertRowEquals(3, ['Branch:', ' All'])
            t.assertRowEquals(4, ['Funding:', ' All'])

            // 1st Group header
            t.assertRowEquals(5, firstFund)
            // 1st Details header
            t.assertRowEquals(6, detailsHeader)
            // Fund: '-' -> Account ID: 32
            t.assertRowEquals(7, detailsFirstFundFirstAccount_20101231)
            // Fund: '-' -> Account ID: 38
            t.assertRowEquals(8, detailsFirstFundSecondAccount_20100723)
            // Fund: '-' -> Account ID: 44
            t.assertRowEquals(9, detailsFirstFundThirdAccount_20101231)
            // 1st Group footer
            t.assertRowEquals(10, subTotalFirstFund_20100723)

            // 2nd Group header
            t.assertRowEquals(11, secondFund)
            // 2nd Details header
            t.assertRowEquals(12, detailsHeader)
            // Fund: 'Funding Org A' -> Account ID: 4
            t.assertRowEquals(13, detailsSecondFundFirstAccount_20101231)
            // 2nd Group footer
            t.assertRowEquals(14, subTotalSecondFund_20101231)

            // Page footer
            t.assertRowEquals(15, pageFooter)
        }
    }
}