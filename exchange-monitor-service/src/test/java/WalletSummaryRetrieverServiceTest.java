import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import work.hoodie.exchange.monitor.common.Balance;
import work.hoodie.exchange.monitor.common.WalletSummary;
import work.hoodie.exchange.monitor.service.balance.BalanceRetrieverService;
import work.hoodie.exchange.monitor.service.wallet.WalletSummaryFactory;
import work.hoodie.exchange.monitor.service.wallet.WalletSummaryRetrieverService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletSummaryRetrieverServiceTest {
    @InjectMocks
    private WalletSummaryRetrieverService walletSummaryRetrieverService;
    @Mock
    private WalletSummaryFactory walletSummaryFactory;
    @Mock
    private BalanceRetrieverService balanceRetrieverService;
    @Mock
    private WalletSummary mockSummary;


    @Test
    public void testGetWallets() throws Exception {

        Balance balance1 = new Balance()
                .setAvailable(BigDecimal.ONE)
                .setOnOrder(BigDecimal.TEN)
                .setCurrency("Sean");
        List<Balance> balances = new ArrayList<Balance>();
        balances.add(balance1);

        when(walletSummaryFactory.getSummary(balance1))
                .thenReturn(mockSummary);
        when(balanceRetrieverService.getBalances())
                .thenReturn(balances);

        List<WalletSummary> actual = walletSummaryRetrieverService.getWalletSummaries();

        assertNotNull(actual);
        verify(walletSummaryFactory).getSummary(balance1);
        assertFalse(actual.isEmpty());
        for (WalletSummary walletSummary : actual) {
            assertEquals(mockSummary, walletSummary);
        }

    }
}
